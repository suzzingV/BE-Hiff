package hiff.hiff.behiff.global.auth.application;

import hiff.hiff.behiff.domain.user.application.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.auth.domain.Token;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.infrastructure.TokenRepository;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.auth.presentation.dto.req.FcmTokenRequest;
import hiff.hiff.behiff.global.auth.presentation.dto.req.LoginRequest;
import hiff.hiff.behiff.global.auth.presentation.dto.res.LoginResponse;
import hiff.hiff.behiff.global.auth.presentation.dto.res.TokenResponse;
import hiff.hiff.behiff.global.common.webClient.WebClientUtils;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import hiff.hiff.behiff.global.util.Parser;
import hiff.hiff.behiff.global.util.FileReader;
import java.security.PrivateKey;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
        String socialId = Parser.getAppleIdByIdToken(request.getIdToken());
        String socialInfo = socialType.getPrefix() + " " + socialId;

        String accessToken = jwtService.createAccessToken(socialInfo);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, socialInfo);

        return userRepository.findBySocialTypeAndSocialId(socialType, socialId)
            .map(user -> {
                user.updateAge();
                userServiceFacade.updatePos(user.getId(), request.getLatitude(), request.getLongitude());
                boolean isFilled = false;
                boolean isAuthorized = false;
                if(user.getPhoneNum() != null) {
                    isAuthorized = true;
                }
                if(user.getNickname() != null) {
                    isFilled = true;
                }
                return LoginResponse.of(accessToken, refreshToken, isAuthorized, isFilled, user.getId());
            })
            .orElseGet(() -> {
                User newUser = userServiceFacade.registerUser(Role.USER, socialId, socialType,
                    request.getLatitude(), request.getLongitude());
                if(socialType == SocialType.APPLE) {
                    String appleRefreshToken = getAppleRefreshToken(request.getAuthorizationCode());
                    saveRefreshToken(newUser, appleRefreshToken);
                }
                return LoginResponse.of(accessToken, refreshToken, false, false, newUser.getId());
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

    public UserUpdateResponse updateFcmToken(Long userId, FcmTokenRequest request) {
        Token token = tokenRepository.findByUserId(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.TOKEN_NOT_FOUND));
        token.updateFcmToken(request.getFcmToken());

        return UserUpdateResponse.from(userId);
    }

    private String checkRefreshToken(String refreshToken) {
        jwtService.isTokenValid(refreshToken);
        return jwtService.checkRefreshToken(refreshToken);
    }

    private String getAppleRefreshToken(String authorizationCode) {
            String clientSecret = createClientSecret();
            Map tokenResponse = WebClientUtils.getAppleToken(clientSecret, authorizationCode, appleIdentifier);
            String refreshToken = tokenResponse.get("refresh_token").toString();
            return refreshToken;
    }

    private String createClientSecret() {
        String keyFile = FileReader.readAppleKeyFile(appleKeyFile);
        PrivateKey privateKey = Parser.getPrivateKeyFromPem(keyFile);
        return jwtService.createClientSecret(privateKey);
    }

    private void saveRefreshToken(User newUser, String appleRefreshToken) {
        Token newToken = Token.builder()
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
