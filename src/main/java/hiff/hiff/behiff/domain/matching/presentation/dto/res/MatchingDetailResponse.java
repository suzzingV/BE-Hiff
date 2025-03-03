package hiff.hiff.behiff.domain.matching.presentation.dto.res;

import hiff.hiff.behiff.domain.matching.domain.enums.MatchingStatus;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.domain.enums.Mbti;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchingDetailResponse {

    private Long matcherId;

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

    private MatchingStatus status;

    private Integer coupon;

    private String phoneNum;

    public static MatchingDetailResponse of(Long matcherId, UserProfile matchedProfile, List<String> photos, List<UserIntroductionDto> introductions, MatchingStatus status) {
        return MatchingDetailResponse.builder()
                .matchedId(matcherId)
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
                .status(status)
                .introductions(introductions)
            .build();
    }

    public static MatchingDetailResponse of(Long matcherId, UserProfile matchedProfile, List<String> photos, List<UserIntroductionDto> introductions, MatchingStatus status, Integer coupon) {
        return MatchingDetailResponse.builder()
                .matchedId(matcherId)
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
                .status(status)
                .introductions(introductions)
                .coupon(coupon)
                .build();
    }

    public static MatchingDetailResponse of(Long matcherId, UserProfile matchedProfile, List<String> photos, List<UserIntroductionDto> introductions, MatchingStatus status, String phoneNum) {
        return MatchingDetailResponse.builder()
                .matchedId(matcherId)
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
                .status(status)
                .introductions(introductions)
                .phoneNum(phoneNum)
                .build();
    }
}
