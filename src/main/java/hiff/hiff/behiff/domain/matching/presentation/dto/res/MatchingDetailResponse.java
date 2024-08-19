package hiff.hiff.behiff.domain.matching.presentation.dto.res;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingScoreDto;
import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Income;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

    private Income matchedIncome;

    private Integer incomeSimilarity;

    private List<NameWithCommonDto> hobbies;

    private Integer hobbySimilarity;

    private List<NameWithCommonDto> lifeStyles;

    private Integer lifeStyleSimilarity;

    public static MatchingDetailResponse of(User matcher, User matched, Double distance,
                                            List<String> photos, MatchingScoreDto matchingScores, List<NameWithCommonDto> hobbies,
                                            List<NameWithCommonDto> lifeStyles) {
        return MatchingDetailResponse.builder()
                .matchedId(matched.getId())
                .nickname(matched.getNickname())
                .age(matched.getAge())
                .distance(distance)
                .photos(photos)
                .totalScore(matchingScores.getTotalScore())
                .myMbti(matcher.getMbti())
                .matchedMbti(matched.getMbti())
                .mbtiSimilarity(matchingScores.getMbtiSimilarity())
                .matchedIncome(matched.getIncome())
                .incomeSimilarity(matchingScores.getIncomeSimilarity())
                .hobbies(hobbies)
                .hobbySimilarity(matchingScores.getHobbySimilarity())
                .lifeStyles(lifeStyles)
                .lifeStyleSimilarity(matchingScores.getLifeStyleSimilarity())
                .build();
    }
}
