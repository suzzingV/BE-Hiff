package hiff.hiff.behiff.global.auth.jwt.service;

import static hiff.hiff.behiff.global.common.redis.RedisService.NOT_EXIST;
import static hiff.hiff.behiff.global.common.webClient.WebClientUtils.APPLE_URL;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String SOURCE_CLAIM = "source";
    private static final String LOGOUT = "logout";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final RedisService redisService;

    public String createAccessToken(String source) {
        Date now = new Date();
        return JWT.create()
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
            .withClaim(SOURCE_CLAIM, source)
            .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
            .sign(Algorithm.HMAC512(secretKey));
    }

    public String reissueRefreshToken(String refreshToken, String email) {
        deleteRefreshToken(refreshToken);
        String reissuedRefreshToken = createRefreshToken();
        updateRefreshToken(reissuedRefreshToken, email);
        return reissuedRefreshToken;
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
            .filter(header -> header.startsWith(BEARER))
            .map(header -> header.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
            .filter(accessToken -> accessToken.startsWith(BEARER))
            .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Optional<String> extractSource(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken) //검증
                .getClaim(SOURCE_CLAIM) //추출
                .asString());
        } catch (JWTVerificationException e) {
            throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
    }

    //RefreshToken redis 저장
    public void updateRefreshToken(String refreshToken, String source) {
        redisService.setValue(refreshToken, source,
            Duration.ofMillis(refreshTokenExpirationPeriod));
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_TOKEN);
        }
    }

    public void deleteRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
        redisService.delete(refreshToken);
    }

    public void invalidAccessToken(String accessToken) {
        redisService.setValue(accessToken, LOGOUT,
            Duration.ofMillis(accessTokenExpirationPeriod));
    }

    public String checkRefreshToken(String refreshToken) {
        if (redisService.getStrValue(refreshToken).equals(NOT_EXIST)) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_REFRESH_TOKEN);
        }
        return redisService.getStrValue(refreshToken);
    }
}