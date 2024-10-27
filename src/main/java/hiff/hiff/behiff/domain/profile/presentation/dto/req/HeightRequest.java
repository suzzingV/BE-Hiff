package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class HeightRequest {

    @Min(0)
    @Max(300)
    @NotNull
    private Integer height;
}
