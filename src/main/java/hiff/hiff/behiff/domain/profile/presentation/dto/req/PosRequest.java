package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PosRequest {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
