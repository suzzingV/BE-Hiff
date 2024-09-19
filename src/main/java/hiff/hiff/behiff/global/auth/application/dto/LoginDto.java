package hiff.hiff.behiff.global.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {

    private String socialId;

    private String refreshToken;

    public static LoginDto of(String socialId, String refreshToken) {
        return LoginDto.builder()
            .refreshToken(refreshToken)
            .socialId(socialId)
            .build();
    }
}
