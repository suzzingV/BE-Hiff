package hiff.hiff.behiff.domain.matching.presentation.dto.res;

import hiff.hiff.behiff.domain.matching.application.dto.MatchingInfoDto;
import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HiffMatchingDetailResponse {


    private Long matchedId;

    private String nickname;

    private Integer age;

    private Double distance;

    private String mainPhoto;

    private List<String> photos;

    private Integer totalScoreByMe;

    private Mbti myMbti;

    private Mbti matchedMbti;

    private Integer mbtiSimilarity;

//    private Income matchedIncome;

//    private Integer incomeSimilarity;

    private List<NameWithCommonDto> hobbies;

    private Integer hobbySimilarity;

    private List<NameWithCommonDto> lifeStyles;

    private Integer lifeStyleSimilarity;

    public static HiffMatchingDetailResponse of(User matcher, User matched, Double distance,
        String mainPhoto,
        List<String> photos, MatchingInfoDto matchingInfo, List<NameWithCommonDto> hobbies,
        List<NameWithCommonDto> lifeStyles) {
        return HiffMatchingDetailResponse.builder()
            .matchedId(matched.getId())
            .nickname(matched.getNickname())
            .age(matched.getAge())
            .distance(distance)
            .photos(photos)
            .totalScoreByMe(matchingInfo.getTotalScoreByMatcher())
            .myMbti(matcher.getMbti())
            .matchedMbti(matched.getMbti())
            .mbtiSimilarity(matchingInfo.getMbtiSimilarity())
//            .matchedIncome(matched.getIncome())
//            .incomeSimilarity(matchingScores.getIncomeSimilarity())
            .hobbies(hobbies)
            .hobbySimilarity(matchingInfo.getHobbySimilarity())
            .lifeStyles(lifeStyles)
            .lifeStyleSimilarity(matchingInfo.getLifeStyleSimilarity())
            .mainPhoto(mainPhoto)
            .build();
    }
}
