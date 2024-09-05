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

    public static LoginResponse of(String accessToken, String refreshToken, Boolean isNew,
        Long userId) {
        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .isNew(isNew)
            .userId(userId)
            .build();
    }

    public void changeUserId(Long userId) {
        this.userId = userId;
    }
}
