package hiff.hiff.behiff.domain.matching.presentation.dto.res;

import hiff.hiff.behiff.domain.user.domain.entity.LifeStyle;
import hiff.hiff.behiff.domain.user.domain.enums.Income;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchingDetailResponse {

    private Long matchedId;

    private String nickname;

    private Integer age;

    private Double distance;

    private List<String> photos;

    private Integer totalScore;

    private Mbti myMbti;

    private Mbti matchedMbti;

    private Integer mbtiSimilarity;

    private Income myIncome;

    private Income matchedIncome;

    private Integer incomeSimilarity;

    private List<NameWithCommonDto> hobbies;

    private Integer hobbySimilarity;

    private List<NameWithCommonDto> lifeStyles;

    private Integer lifeStyleSimilarity;
}
