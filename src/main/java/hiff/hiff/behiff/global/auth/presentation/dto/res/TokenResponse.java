package hiff.hiff.behiff.global.auth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private String email;
    private Boolean isJoined;

    public static TokenResponse of(String accessToken, String refreshToken,
                                   String email, boolean isJoined) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(email)
                .isJoined(isJoined)
                .build();
    }
}
