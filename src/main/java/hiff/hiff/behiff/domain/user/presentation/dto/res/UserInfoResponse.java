package hiff.hiff.behiff.domain.user.presentation.dto.res;

import hiff.hiff.behiff.domain.user.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserCareer;
import hiff.hiff.behiff.domain.user.domain.entity.UserGrad;
import hiff.hiff.behiff.domain.user.domain.entity.UserIncome;
import hiff.hiff.behiff.domain.user.domain.entity.UserUniversity;
import hiff.hiff.behiff.domain.weighting.domain.entity.Weighting;
import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

    private Long userId;

    private String nickname;

    private LocalDate birth;

    private Integer age;

    private Integer maxDistance;

    private Integer minDistance;

    private String mainPhoto;

    private List<String> photos;

    private List<String> hobbies;

    private List<String> lifeStyles;

    private Integer appearanceWeight;

    private Integer hobbyWeight;

    private Integer lifeStyleWeight;

    private Integer mbtiWeight;

    private String phoneNum;

    private String gender;

    private Mbti mbti;

    private Education education;

    private String school;

    private Integer hopeMinAge;

    private Integer hopeMaxAge;

    private Integer heart;

    private Double appearanceScore;

    private Boolean isSmoking;

    private String drinking;

    private String buddy;

    private String religion;

    private String ideology;

    private String contactFrequency;

    private String conflictResolution;

    private Integer height;

    private String bodyType;

    private List<String> fashions;

    private List<UserIntroductionDto> introductions;

    private UserCareer career;

    private UserUniversity university;

    private UserGrad grad;

    private UserIncome income;

    public static UserInfoResponse of(User user, List<String> hobbies, String mainPhoto,
        List<String> photos,
        List<String> lifeStyles, Weighting weighting, List<String> fashions,
        List<UserIntroductionDto> introductions, UserCareer userCareer, UserUniversity userUniversity, UserGrad userGrad, UserIncome userIncome) {
        return UserInfoResponse.builder()
            .userId(user.getId())
            .nickname(user.getNickname())
            .birth(user.getBirth())
            .age(user.getAge())
            .mainPhoto(mainPhoto)
            .photos(photos)
            .hobbies(hobbies)
            .lifeStyles(lifeStyles)
            .lifeStyleWeight(weighting.getLifeStyle())
            .hobbyWeight(weighting.getHobby())
            .appearanceWeight(weighting.getAppearance())
            .mbtiWeight(weighting.getMbti())
            .phoneNum(user.getPhoneNum())
            .gender(user.getGenderText())
            .mbti(user.getMbti())
            .appearanceScore(user.getEvaluatedScore())
            .isSmoking(user.getIsSmoking())
            .drinking(user.getDrinkingText())
            .buddy(user.getBuddyText())
            .religion(user.getReligionText())
            .ideology(user.getIdeologyText())
            .contactFrequency(user.getContactFrequencyText())
            .conflictResolution(user.getConflictResolutionText())
            .height(user.getHeight())
            .bodyType(user.getBodyTypeText())
            .fashions(fashions)
            .introductions(introductions)
            .career(userCareer)
            .university(userUniversity)
            .grad(userGrad)
            .income(userIncome)
            .build();
    }
}
