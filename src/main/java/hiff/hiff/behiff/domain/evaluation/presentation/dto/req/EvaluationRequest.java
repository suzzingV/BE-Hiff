package hiff.hiff.behiff.domain.evaluation.presentation.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EvaluationRequest {

    @NotEmpty
    private Long evaluatedId;

    @NotEmpty
    @Min(1)
    @Max(5)
    private Integer score;
}
