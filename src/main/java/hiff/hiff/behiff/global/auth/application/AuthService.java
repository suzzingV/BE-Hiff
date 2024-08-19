package hiff.hiff.behiff.global.auth.application;

import hiff.hiff.behiff.domain.evaluation.application.EvaluationService;
import hiff.hiff.behiff.domain.user.application.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.auth.presentation.dto.req.LoginRequest;
import hiff.hiff.behiff.global.auth.presentation.dto.res.LoginResponse;
import hiff.hiff.behiff.global.auth.presentation.dto.res.TokenResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EvaluationService evaluationService;
    private final UserService userService;

    public TokenResponse createTokens(LoginRequest request) {
        String email = request.getEmail();

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, email);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public LoginResponse login(LoginRequest request, TokenResponse tokenResponse) {
        String email = request.getEmail();
        SocialType socialType = request.getSocialType();
        String socialId = request.getSocialId();

        return userRepository.findByEmail(email)
                .map(user -> {
                    user.updateAge();
                    return LoginResponse.of(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), false, email);
                })
                .orElseGet(() -> {
                    User newUser = userService.registerUser(email, socialId, socialType, Role.USER);
                    newUser.updateAge();
                    evaluationService.addEvaluatedUser(newUser.getId(), newUser.getGender());
                    evaluationService.addEvaluatedUser(newUser.getId(), newUser.getGender());
                    return LoginResponse.of(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), true, email);
                });
    }

    public LoginResponse updatePos(LoginRequest request, LoginResponse response) {
        User user = userService.findByEmail(request.getEmail());
        response.changeUserId(user.getId());

        if (response.getIsNew()) {
            userService.createPos(response.getUserId(), request.getPosX(), request.getPosY());
        } else {
            userService.updatePos(response.getUserId(), request.getPosX(), request.getPosY());
        }

        return response;
    }

    public TokenResponse reissueTokens(Optional<String> refresh) {
        String refreshToken = refresh
                .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        String email = checkRefreshToken(refreshToken);
        String reissuedAccessToken = jwtService.createAccessToken(email);
        String reissuedRefreshToken = jwtService.reissueRefreshToken(refreshToken, email);

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

    private String checkRefreshToken(String refreshToken) {
        jwtService.isTokenValid(refreshToken);
        return jwtService.checkRefreshToken(refreshToken);
    }
}
