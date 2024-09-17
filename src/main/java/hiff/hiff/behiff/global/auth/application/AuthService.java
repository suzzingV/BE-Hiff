package hiff.hiff.behiff.global.auth.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import hiff.hiff.behiff.domain.user.application.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.application.dto.AppleLoginDto;
import hiff.hiff.behiff.global.auth.domain.Token;
import hiff.hiff.behiff.global.auth.exception.AuthException;
import hiff.hiff.behiff.global.auth.infrastructure.TokenRepository;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.auth.presentation.dto.req.LoginRequest;
import hiff.hiff.behiff.global.auth.presentation.dto.res.LoginResponse;
import hiff.hiff.behiff.global.auth.presentation.dto.res.TokenResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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
        String socialId;
        String appleRefreshToken;
        if(socialType == SocialType.APPLE) {
            AppleLoginDto appleLoginDto = authorizeAppleUser(request.getAuthorizationCode());
            socialId = appleLoginDto.getSocialId();
            appleRefreshToken = appleLoginDto.getRefreshToken();
        } else {
            appleRefreshToken = null;
            socialId = request.getSocialId();
        }

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
                return LoginResponse.of(accessToken, refreshToken, false, user.getId());
            })
            .orElseGet(() -> {
                User newUser = userServiceFacade.registerUser(Role.USER, socialId, socialType,
                    request.getLatitude(), request.getLongitude());
                saveTokens(request, newUser, appleRefreshToken);
                return LoginResponse.of(accessToken, refreshToken, true, newUser.getId());
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

    public void deleteToken(Long userId) {
        Token token = findTokenByUserId(userId);
        tokenRepository.delete(token);
    }

    private String checkRefreshToken(String refreshToken) {
        jwtService.isTokenValid(refreshToken);
        return jwtService.checkRefreshToken(refreshToken);
    }

    private AppleLoginDto authorizeAppleUser(String authorizationCode) {
        try {
            InputStream inputStream = ResourceUtils.getURL(appleKeyFile).openStream();
            if(inputStream == null) {
                log.info("input stream null" + appleKeyFile);
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
            headerParamsMap.put("kid", appleKeyId);
            headerParamsMap.put("alg", "ES256");

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
                        .path("/auth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", appleIdentifier)
                        .queryParam("client_secret", clientSecretKey)
                        .queryParam("code", authorizationCode)
                        .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            String idToken = (String) tokenResponse.get("id_token");

            Map<String, Object> keyReponse =
                webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                        .path("/auth/keys")
                        .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            List<Map<String, Object>> keys = (List<Map<String, Object>>) keyReponse.get("keys");

            SignedJWT signedJWT = SignedJWT.parse(idToken);
            for (Map<String, Object> key : keys) {
                RSAKey rsaKey = (RSAKey) JWK.parse(new ObjectMapper().writeValueAsString(key));
                RSAPublicKey rsaPublicKey = rsaKey.toRSAPublicKey();
                JWSVerifier jwsVerifier = new RSASSAVerifier(rsaPublicKey);

                // idToken을 암호화한 key인 경우
                if (signedJWT.verify(jwsVerifier)) {
                    // jwt를 .으로 나눴을때 가운데에 있는 payload 확인
                    String payload = idToken.split("[.]")[1];
                    // public key로 idToken 복호화
                    Map<String, Object> payloadMap = new ObjectMapper().readValue(new String(
                        Base64.getDecoder().decode(payload)), Map.class);
                    // 사용자 이메일 정보 추출

                    // 결과 반환
                    return AppleLoginDto.of(payloadMap.get("sub").toString(), tokenResponse.get("refreshToken").toString());
                }
            }
        } catch (Exception e) {
            throw new AuthException(ErrorCode.KEY_FILE_IOEXCEPTION, e.getMessage());
        }
        return null;
    }
    private Mono<Throwable> handleErrorResponse(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
            .flatMap(errorBody -> {
                // 에러 로그 찍기
                System.out.println("Error response body: " + errorBody);
                return Mono.error(new RuntimeException("Error occurred: " + errorBody));
            });
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
