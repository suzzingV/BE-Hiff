package hiff.hiff.behiff.domain.matching.presentation.dto.res;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingInfoDto;
import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.profile.domain.enums.Mbti;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyMatchingDetailResponse {

    private Long matchedId;

    private String nickname;

    private Integer age;

    private Double distance;

    private String mainPhoto;

    private List<String> photos;

    private Integer totalScore;

    private Mbti myMbti;

    private Mbti matchedMbti;

    private Integer mbtiSimilarity;

//    private Income matchedIncome;

//    private Integer incomeSimilarity;

    private List<NameWithCommonDto> hobbies;

    private Integer hobbySimilarity;

    private List<NameWithCommonDto> lifeStyles;

    private Integer lifeStyleSimilarity;

    public static DailyMatchingDetailResponse of(User matcher, User matched, Double distance,
        String mainPhoto,
        List<String> photos, MatchingInfoDto matchingScores, List<NameWithCommonDto> hobbies,
        List<NameWithCommonDto> lifeStyles) {
        return DailyMatchingDetailResponse.builder()
            .matchedId(matched.getId())
//            .nickname(matched.getNickname())
//            .age(matched.getAge())
            .distance(distance)
            .photos(photos)
            .totalScore(matchingScores.getTotalScoreByMatcher())
//            .myMbti(matcher.getMbti())
//            .matchedMbti(matched.getMbti())
            .mbtiSimilarity(matchingScores.getMbtiSimilarity())
//            .matchedIncome(matched.getIncome())
//            .incomeSimilarity(matchingScores.getIncomeSimilarity())
            .hobbies(hobbies)
            .hobbySimilarity(matchingScores.getHobbySimilarity())
            .lifeStyles(lifeStyles)
            .lifeStyleSimilarity(matchingScores.getLifeStyleSimilarity())
            .mainPhoto(mainPhoto)
            .build();
    }
}
