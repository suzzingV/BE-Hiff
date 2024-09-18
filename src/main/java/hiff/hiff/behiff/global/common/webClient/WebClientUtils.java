package hiff.hiff.behiff.global.common.webClient;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientUtils {

    public static final String APPLE_URL = "https://appleid.apple.com";

    private static final WebClient APPLE_WEB_CLIENT = WebClient
        .builder()
        .baseUrl(APPLE_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    public static Map getAppleToken(String clientSecret, String authorizationCode, String appleIdentifier) {
        return APPLE_WEB_CLIENT
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/auth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", appleIdentifier)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", authorizationCode)
                .build())
            .retrieve()
            .bodyToMono(Map.class)
            .block();
    }

    public static Map getAppleKeys() {
        return APPLE_WEB_CLIENT
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/auth/keys")
                .build())
            .retrieve()
            .bodyToMono(Map.class)
            .block();
    }

    public static Map revokeApple(String clientSecret, String refreshToken, String appleIdentifier) {
        return APPLE_WEB_CLIENT
            .post()
            .uri(uriBuilder -> uriBuilder
                .path("/auth/revoke")
                .queryParam("token_type_hint", "refresh_token")
                .queryParam("client_id", appleIdentifier)
                .queryParam("client_secret", clientSecret)
                .queryParam("token", refreshToken)
                .build())
            .retrieve()
            .bodyToMono(Map.class)
            .block();
    }
}
