package hiff.hiff.behiff.global.auth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String email;
    private boolean isJoined;

    public static LoginResponse of(Long userId, String accessToken, String refreshToken,
                                   String email, boolean isJoined) {
        return LoginResponse.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(email)
                .isJoined(isJoined)
                .build();
    }
}
