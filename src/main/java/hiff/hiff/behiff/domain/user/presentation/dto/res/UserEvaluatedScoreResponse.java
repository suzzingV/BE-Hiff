package hiff.hiff.behiff.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserEvaluatedScoreResponse {

    private Long userId;

    private Double evaluatedScore;
}
