package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserHeartRequest {

    @NotNull
    private Integer usage;
}
