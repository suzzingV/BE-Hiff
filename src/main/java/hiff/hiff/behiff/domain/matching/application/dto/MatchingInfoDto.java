package hiff.hiff.behiff.domain.matching.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchingInfoDto {

    private Integer mbtiSimilarity;

    private Integer hobbySimilarity;

    private Integer lifeStyleSimilarity;

//    private Integer incomeSimilarity;

    private Integer totalScore;
}
