package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.evaluation.domain.entity.EvaluatedUser;
import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluatedUserRepository;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.application.AuthService;
import hiff.hiff.behiff.global.auth.domain.Token;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.infrastructure.TokenRepository;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCRUDService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EvaluatedUserRepository evaluatedUserRepository;
    private final RedisService redisService;

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

    public User registerUser(Role role, String socialId, SocialType socialType) {
        User user = User.builder()
            .socialId(socialId)
            .socialType(socialType)
            .role(role)
            .build();
        return userRepository.save(user);
    }


    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public void checkDuplication(String phoneNum) {
        userRepository.findByPhoneNum(phoneNum)
                .ifPresent(user -> {
                    throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
                });
    }

    public void withdraw(User user, Optional<String> access, Optional<String> refresh) {
        if(user.getSocialType() ==SocialType.APPLE) {
            try {
                InputStream inputStream = ResourceUtils.getURL(appleKeyFile).openStream();
                BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
                String readLine = null;
                StringBuilder stringBuilder = new StringBuilder();
                while ((readLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(readLine);
                    stringBuilder.append("\n");
                }
                String keyFile = stringBuilder.toString();

                Reader reader = new StringReader(keyFile);
                PEMParser pemParser = new PEMParser(reader);
                JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
                PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
                PrivateKey privateKey = jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);

                Map<String, Object> headerParamsMap = new HashMap<>();

                Date expirationDate = Date.from(
                    LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
                String clientSecretKey = Jwts
                    .builder()
                    .setHeaderParams(headerParamsMap)
                    .setIssuer(appleTeamId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .setAudience("https://appleid.apple.com")
                    .setSubject(appleIdentifier)
                    .signWith(SignatureAlgorithm.ES256, privateKey)
                    .compact();
                WebClient webClient =
                    WebClient
                        .builder()
                        .baseUrl("https://appleid.apple.com")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build();

                Map tokenResponse =
                    webClient
                        .post()
                        .uri(uriBuilder -> uriBuilder
                            .path("/auth/revoke")
                            .queryParam("token_type_hint", "refresh_token")
                            .queryParam("client_id", appleIdentifier)
                            .queryParam("client_secret", clientSecretKey)
                            .queryParam("token", redisService.getStrValue("ref_" + user.getSocialId()))
                            .build())
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> handleErrorResponse(clientResponse))
                        .bodyToMono(Map.class)
                        .block();
            } catch (Exception e) {
                throw new AuthException(ErrorCode.SERVER_ERROR);
            }
        }
        List<EvaluatedUser> evaluatedUsers = evaluatedUserRepository.findByUserId(user.getId());
        evaluatedUserRepository.deleteAll(evaluatedUsers);
        user.delete();

        String accessToken = access.orElseThrow(
            () -> new AuthException(ErrorCode.ACCESS_TOKEN_REQUIRED));
        String refreshToken = refresh.orElseThrow(
            () -> new AuthException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        jwtService.isTokenValid(refreshToken);
        jwtService.isTokenValid(accessToken);
        jwtService.deleteRefreshToken(refreshToken);
        jwtService.invalidAccessToken(accessToken);
    }

    private Mono<Throwable> handleErrorResponse(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
            .flatMap(errorBody -> {
                // 에러 로그 찍기
                System.out.println("Error response body: " + errorBody);
                return Mono.error(new RuntimeException("Error occurred: " + errorBody));
            });
    }
}
