package hiff.hiff.behiff.domain.matching.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchingInfoDto {

    private Long matcherId;

    private Long matchedId;

    private Integer mbtiSimilarity;

    private Integer hobbySimilarity;

    private Integer lifeStyleSimilarity;

//    private Integer incomeSimilarity;

    private Integer totalScoreByMatcher;

    private Integer totalScoreByMatched;
}
