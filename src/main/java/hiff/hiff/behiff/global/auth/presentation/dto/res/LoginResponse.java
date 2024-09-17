package hiff.hiff.behiff.global.auth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private Boolean isNew;
    private Boolean isFilled;

    public static LoginResponse of(String accessToken, String refreshToken, Boolean isNew, Boolean isFilled,
        Long userId) {
        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .isNew(isNew)
            .userId(userId)
            .isFilled(isFilled)
            .build();
    }

    public void changeUserId(Long userId) {
        this.userId = userId;
    }
}
