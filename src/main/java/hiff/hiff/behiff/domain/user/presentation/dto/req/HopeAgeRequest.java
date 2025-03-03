package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class HopeAgeRequest {

    @Min(20)
    @Max(50)
    @NotNull
    private Integer minAge;

    @Min(20)
    @Max(50)
    @NotNull
    private Integer maxAge;
}
