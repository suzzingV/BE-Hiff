package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WeightValueRequest {

//    @NotNull
//    @Min(1)
//    @Max(5)
//    private Integer income;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer appearance;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer hobby;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer lifeStyle;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer mbti;
}
