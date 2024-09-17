package hiff.hiff.behiff.global.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppleLoginDto {

    private String socialId;

    private String refreshToken;

    public static AppleLoginDto of(String socialId, String refreshToken) {
        return AppleLoginDto.builder()
            .refreshToken(refreshToken)
            .socialId(socialId)
            .build();
    }
}
