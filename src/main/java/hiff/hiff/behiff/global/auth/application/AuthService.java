package hiff.hiff.behiff.global.auth.application;

import hiff.hiff.behiff.domain.user.application.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.application.dto.LoginDto;
import hiff.hiff.behiff.global.auth.domain.Token;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.infrastructure.TokenRepository;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.auth.presentation.dto.req.LoginRequest;
import hiff.hiff.behiff.global.auth.presentation.dto.res.LoginResponse;
import hiff.hiff.behiff.global.auth.presentation.dto.res.TokenResponse;
import hiff.hiff.behiff.global.common.webClient.WebClientUtils;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import hiff.hiff.behiff.global.util.Parser;
import hiff.hiff.behiff.global.util.FileReader;
import java.security.PrivateKey;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserServiceFacade userServiceFacade;
    private final TokenRepository tokenRepository;

    @Value("${apple.redirect-url}")
    private String appleRedirectUrl;
    @Value("${apple.credentials.key-id}")
    private String appleKeyId;
    @Value("${apple.credentials.location}")
    private String appleKeyFile;
    @Value("${apple.credentials.identifier}")
    private String appleIdentifier;
    @Value("${apple.credentials.team-id}")
    private String appleTeamId;

    public LoginResponse login(LoginRequest request) {
        SocialType socialType = request.getSocialType();
        LoginDto loginDto = getSocialInfoByType(request.getIdToken(), socialType,
            request.getAuthorizationCode());
        String socialId = loginDto.getSocialId();
        String socialInfo = socialType.getPrefix() + "_" + socialId;

        String accessToken = jwtService.createAccessToken(socialInfo);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, socialInfo);

        return userRepository.findBySocialTypeAndSocialId(socialType, socialId)
            .map(user -> {
                user.updateAge();
                userServiceFacade.updatePos(user.getId(), request.getLatitude(), request.getLongitude());
                Token token = findTokenByUserId(user.getId());
                token.updateFcmToken(request.getFcmToken());
                boolean isFilled = false;
                if(user.getNickname() != null) {
                    isFilled = true;
                }
                return LoginResponse.of(accessToken, refreshToken, false, isFilled, user.getId());
            })
            .orElseGet(() -> {
                User newUser = userServiceFacade.registerUser(Role.USER, socialId, socialType,
                    request.getLatitude(), request.getLongitude());
                saveTokens(request, newUser, loginDto.getRefreshToken());
                return LoginResponse.of(accessToken, refreshToken, true, false, newUser.getId());
            });
    }

    public TokenResponse reissueTokens(Optional<String> refresh) {
        String refreshToken = refresh
            .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        String socialInfo = checkRefreshToken(refreshToken);
        String reissuedAccessToken = jwtService.createAccessToken(socialInfo);
        String reissuedRefreshToken = jwtService.reissueRefreshToken(refreshToken, socialInfo);

        return TokenResponse.builder()
            .accessToken(reissuedAccessToken)
            .refreshToken(reissuedRefreshToken)
            .build();
    }

    public void logout(Optional<String> access, Optional<String> refresh) {
        String accessToken = access.orElseThrow(
            () -> new AuthException(ErrorCode.ACCESS_TOKEN_REQUIRED));
        String refreshToken = refresh.orElseThrow(
            () -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        jwtService.isTokenValid(refreshToken);
        jwtService.isTokenValid(accessToken);
        jwtService.deleteRefreshToken(refreshToken);
        jwtService.invalidAccessToken(accessToken);
    }

    private LoginDto getSocialInfoByType(String idToken, SocialType socialType, String authorizationCode) {
        if(socialType == SocialType.APPLE) {
            return authorizeAppleUser(authorizationCode);
        }
        String socialId = Parser.getSocialIdByIdToken(idToken);
        return LoginDto.of(socialId, null);
    }

    private String checkRefreshToken(String refreshToken) {
        jwtService.isTokenValid(refreshToken);
        return jwtService.checkRefreshToken(refreshToken);
    }

    private LoginDto authorizeAppleUser(String authorizationCode) {
            String clientSecret = createClientSecret();
            Map tokenResponse = WebClientUtils.getAppleToken(clientSecret, authorizationCode, appleIdentifier);
            String idToken = tokenResponse.get("id_token").toString();
            String refreshToken = tokenResponse.get("refreshToken").toString();
            Map keyResponse = WebClientUtils.getAppleKeys();
            List<Map<String, Object>> keys = (List<Map<String, Object>>) keyResponse.get("keys");
            String socialId = Parser.getAppleIdByIdToken(keys, idToken);
            return LoginDto.of(socialId, refreshToken);
    }

    private String createClientSecret() {
        String keyFile = FileReader.readAppleKeyFile(appleKeyFile);
        PrivateKey privateKey = Parser.getPrivateKeyFromPem(keyFile);
        return jwtService.createClientSecret(privateKey);
    }

    private void saveTokens(LoginRequest request, User newUser, String appleRefreshToken) {
        Token newToken = Token.builder()
            .fcmToken(request.getFcmToken())
            .userId(newUser.getId())
            .appleRefreshToken(appleRefreshToken)
            .build();
        tokenRepository.save(newToken);
    }

    public Token findTokenByUserId(Long userId) {
        return tokenRepository.findByUserId(userId)
            .orElseThrow(() -> new AuthException(ErrorCode.TOKEN_NOT_FOUND));
    }
}
