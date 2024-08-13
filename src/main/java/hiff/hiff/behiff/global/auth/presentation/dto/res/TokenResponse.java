package hiff.hiff.behiff.global.auth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private String email;
    private Boolean isNew;

    public static TokenResponse of(String accessToken, String refreshToken,
        String email, boolean isNew) {
        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .email(email)
            .isNew(isNew)
            .build();
    }
}
