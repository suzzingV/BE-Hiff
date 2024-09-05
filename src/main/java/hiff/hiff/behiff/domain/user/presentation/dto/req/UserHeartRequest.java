package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserHeartRequest {

    @NotEmpty
    private Integer usage;
}
