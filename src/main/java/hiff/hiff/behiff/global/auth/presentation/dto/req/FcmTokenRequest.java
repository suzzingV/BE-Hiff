package hiff.hiff.behiff.global.auth.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FcmTokenRequest {

    @NotEmpty
    private String fcmToken;
}
