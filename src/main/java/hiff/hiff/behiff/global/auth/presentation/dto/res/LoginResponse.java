package hiff.hiff.behiff.global.auth.presentation.dto.res;

import hiff.hiff.behiff.domain.user.presentation.dto.res.UserInfoResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private UserInfoResponse userInfoResponse;

    public static LoginResponse of(String accessToken, String refreshToken, UserInfoResponse userInfoResponse) {
        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .userInfoResponse(userInfoResponse)
            .build();
    }
}
