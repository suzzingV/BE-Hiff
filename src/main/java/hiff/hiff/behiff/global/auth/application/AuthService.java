package hiff.hiff.behiff.global.auth.application;

import hiff.hiff.behiff.domain.user.application.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.auth.presentation.dto.req.LoginRequest;
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

    public final JwtService jwtService;
    public final UserRepository userRepository;
    public final UserService userService;

    public TokenResponse login(LoginRequest request) {
        String email = request.getEmail();
        SocialType socialType = request.getSocialType();
        String socialId = request.getSocialId();

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, email);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            User newUser = userService.registerUser(email, socialId,
                    socialType, Role.USER);
            newUser.updateAge();
            return TokenResponse.of(accessToken, refreshToken, email, false);
        }

        User user = userOptional.get();
        user.updateAge();
        return TokenResponse.of(accessToken, refreshToken, email, true);
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
                .isJoined(true)
                .email(email)
                .build();
    }

    private String checkRefreshToken(String refreshToken) {
        jwtService.isTokenValid(refreshToken);
        return jwtService.checkRefreshToken(refreshToken);
    }

    public void logout(Optional<String> access, Optional<String> refresh) {
        String accessToken = access.orElseThrow(() -> new AuthException(ErrorCode.ACCESS_TOKEN_REQUIRED));
        String refreshToken = refresh.orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        jwtService.isTokenValid(refreshToken);
        jwtService.isTokenValid(accessToken);
        //refresh token 삭제
        jwtService.deleteRefreshToken(refreshToken);
        //access token blacklist 처리 -> 로그아웃한 사용자가 요청 시 access token이 redis에 존재하면 jwtAuthenticationProcessingFilter에서 인증처리 거부
        jwtService.invalidAccessToken(accessToken);
    }
}
