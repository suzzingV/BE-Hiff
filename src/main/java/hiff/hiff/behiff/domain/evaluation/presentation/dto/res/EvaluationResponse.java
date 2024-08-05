package hiff.hiff.behiff.domain.evaluation.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EvaluationResponse {

    private Long evaluatedId;

    private String photo;
}
