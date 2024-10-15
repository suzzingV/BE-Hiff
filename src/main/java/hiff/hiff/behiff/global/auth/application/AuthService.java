package hiff.hiff.behiff.global.auth.application;

import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import hiff.hiff.behiff.domain.user.application.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PhoneNumRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.auth.domain.Token;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.infrastructure.TokenRepository;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.auth.presentation.dto.req.FcmTokenRequest;
import hiff.hiff.behiff.global.auth.presentation.dto.req.LoginRequest;
import hiff.hiff.behiff.global.auth.presentation.dto.res.CodeResponse;
import hiff.hiff.behiff.global.auth.presentation.dto.res.LoginResponse;
import hiff.hiff.behiff.global.auth.presentation.dto.res.TokenResponse;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.common.sms.SmsUtil;
import hiff.hiff.behiff.global.response.properties.ErrorCode;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hiff.hiff.behiff.domain.user.util.VerificationCodeGenerator.getCode;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserServiceFacade userServiceFacade;
    private final TokenRepository tokenRepository;
    private final SmsUtil smsUtil;
    private final RedisService redisService;
    private final UserCRUDService userCRUDService;

    private static final Duration IDENTIFY_VERIFICATION_DURATION = Duration.ofMinutes(5);
    private static final String IDENTIFY_VERIFICATION_PREFIX = "verify_";

    public LoginResponse login(LoginRequest request) {
        String accessToken = jwtService.createAccessToken(request.getPhoneNum());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(refreshToken, request.getPhoneNum());

        return userRepository.findByPhoneNum(request.getPhoneNum())
            .map(user -> {
                user.updateAge();
                userServiceFacade.updatePos(user.getId(), request.getLatitude(), request.getLongitude());
                return LoginResponse.of(accessToken, refreshToken, user);
            })
            .orElseGet(() -> {
                User newUser = userServiceFacade.registerUser(Role.USER, request.getPhoneNum(),
                    request.getLatitude(), request.getLongitude());
                generateTokenContainer(newUser.getId());
                return LoginResponse.of(accessToken, refreshToken, newUser);
            });
    }

    public TokenResponse reissueTokens(Optional<String> refresh) {
        String refreshToken = refresh
            .orElseThrow(() -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));
        String source = checkRefreshToken(refreshToken);
        String reissuedAccessToken = jwtService.createAccessToken(source);
        String reissuedRefreshToken = jwtService.reissueRefreshToken(refreshToken, source);

        return TokenResponse.builder()
            .accessToken(reissuedAccessToken)
            .refreshToken(reissuedRefreshToken)
            .build();
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

    public CodeResponse sendVerificationCode(PhoneNumRequest request) {
        String verificationCode = getCode();

        smsUtil.sendVerificationCode(request.getPhoneNum(), verificationCode);

        log.info("인증 코드: " + verificationCode);
        redisService.setValue(IDENTIFY_VERIFICATION_PREFIX + verificationCode, request.getPhoneNum(),
                IDENTIFY_VERIFICATION_DURATION);
        return CodeResponse.builder()
            .code(verificationCode)
            .build();
    }

    public void checkCode(LoginRequest request) {
        String savedPhoneNum = redisService.getStrValue(IDENTIFY_VERIFICATION_PREFIX + request.getCode());
        if (!(request.getPhoneNum()).equals(savedPhoneNum)) {
            throw new UserException(ErrorCode.VERIFICATION_CODE_INCORRECT);
        }
    }

    public Token findTokenByUserId(Long userId) {
        return tokenRepository.findByUserId(userId)
            .orElseThrow(() -> new AuthException(ErrorCode.TOKEN_NOT_FOUND));
    }

    public void withdraw(User user, Optional<String> accessToken, Optional<String> refreshToken) {
        userCRUDService.deleteUserRecord(user);
        invalidTokens(accessToken, refreshToken);
    }

    public void invalidTokens(Optional<String> access, Optional<String> refresh) {
        String accessToken = access.orElseThrow(
                () -> new AuthException(ErrorCode.ACCESS_TOKEN_REQUIRED));
        String refreshToken = refresh.orElseThrow(
                () -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        jwtService.isTokenValid(refreshToken);
        jwtService.isTokenValid(accessToken);
        jwtService.deleteRefreshToken(refreshToken);
        jwtService.invalidAccessToken(accessToken);
    }

    private void generateTokenContainer(Long userId) {
        Token token = Token.builder()
                .userId(userId)
                .build();
        tokenRepository.save(token);
    }
}
