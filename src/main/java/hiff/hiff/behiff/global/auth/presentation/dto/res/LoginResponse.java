package hiff.hiff.behiff.global.auth.presentation.dto.res;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private User user;

    public static LoginResponse of(String accessToken, String refreshToken, User user) {
        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(user)
            .build();
    }
}
