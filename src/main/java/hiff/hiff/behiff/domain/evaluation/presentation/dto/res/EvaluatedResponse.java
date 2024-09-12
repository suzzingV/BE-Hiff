package hiff.hiff.behiff.domain.evaluation.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EvaluatedResponse {

    private Long evaluatedId;

    private String photo;
}
