package hiff.hiff.behiff.global.auth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private Boolean isAuthorized;
    private Boolean isFilled;

    public static LoginResponse of(String accessToken, String refreshToken, Boolean isAuthorized, Boolean isFilled,
        Long userId) {
        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .isAuthorized(isAuthorized)
            .userId(userId)
            .isFilled(isFilled)
            .build();
    }

    public void changeUserId(Long userId) {
        this.userId = userId;
    }
}
