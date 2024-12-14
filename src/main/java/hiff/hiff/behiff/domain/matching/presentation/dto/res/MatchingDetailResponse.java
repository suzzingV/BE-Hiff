package hiff.hiff.behiff.domain.matching.presentation.dto.res;

import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.domain.enums.Mbti;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchingDetailResponse {

    private Long matchedId;

    private String nickname;

    private Integer age;

//    private Double distance;

    private String mainPhoto;

    private List<String> photos;

//    private Integer totalScore;

//    private Mbti myMbti;

    private Mbti mbti;

//    private Integer mbtiSimilarity;

//    private Income matchedIncome;

//    private Integer incomeSimilarity;

//    private List<NameWithCommonDto> hobbies;

//    private Integer hobbySimilarity;

//    private List<NameWithCommonDto> lifeStyles;

//    private Integer lifeStyleSimilarity;

    private List<UserIntroductionDto> introductions;

    private String location;

    public static MatchingDetailResponse of(UserProfile matchedProfile, List<String> photos, List<UserIntroductionDto> introductions) {
        return MatchingDetailResponse.builder()
            .matchedId(matchedProfile.getId())
//            .nickname(matchedProfile.getNickname())
            .age(matchedProfile.getAge())
//            .distance(distance)
            .photos(photos)
//            .totalScore(matchingScores.getTotalScoreByMatcher())
//            .myMbti(matcher.getMbti())
            .mbti(matchedProfile.getMbti())
                .location(matchedProfile.getLocation())
//            .mbtiSimilarity(matchingScores.getMbtiSimilarity())
//            .matchedIncome(matchedProfile.getIncome())
//            .incomeSimilarity(matchingScores.getIncomeSimilarity())
//            .hobbies(hobbies)
//            .hobbySimilarity(matchingScores.getHobbySimilarity())
//            .lifeStyles(lifeStyles)
//            .lifeStyleSimilarity(matchingScores.getLifeStyleSimilarity())
            .mainPhoto(matchedProfile.getMainPhoto())
            .build();
    }
}
