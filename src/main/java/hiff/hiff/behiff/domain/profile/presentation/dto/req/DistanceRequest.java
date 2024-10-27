package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DistanceRequest {

    @Min(0)
    @Max(700)
    @NotNull
    private Integer maxDistance;

    @Min(0)
    @Max(700)
    @NotNull
    private Integer minDistance;
}
