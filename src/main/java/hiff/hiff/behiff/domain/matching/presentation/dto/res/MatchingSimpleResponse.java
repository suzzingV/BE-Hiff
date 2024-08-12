package hiff.hiff.behiff.domain.matching.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MatchingSimpleResponse {

    private String nickname;

    private Integer age;

    private Integer totalScore;

    private Double distance;

    private String mainPhoto;
}
