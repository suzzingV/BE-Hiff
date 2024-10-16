package hiff.hiff.behiff.domain.user.presentation.dto.res;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.domain.enums.BodyType;
import hiff.hiff.behiff.domain.user.domain.enums.Buddy;
import hiff.hiff.behiff.domain.user.domain.enums.ConflictResolution;
import hiff.hiff.behiff.domain.user.domain.enums.ContactFrequency;
import hiff.hiff.behiff.domain.user.domain.enums.Drinking;
import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Ideology;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import hiff.hiff.behiff.domain.user.domain.enums.Religion;
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

//    private Integer incomeWeight;

    private Integer appearanceWeight;

    private Integer hobbyWeight;

    private Integer lifeStyleWeight;

    private Integer mbtiWeight;

    private String phoneNum;

    private Gender gender;

    private Mbti mbti;

//    private Income income;

    private Education education;

    private String school;

    private String career;

    private Integer hopeMinAge;

    private Integer hopeMaxAge;

    private Integer heart;

    private Double appearanceScore;

    private Boolean isSmoking;

    private Drinking isDrinking;

    private Buddy buddy;

    private Religion religion;

    private Ideology ideology;

    private ContactFrequency contactFrequency;

    private ConflictResolution conflictResolution;

    private Integer height;

    private BodyType bodyType;

    public static UserInfoResponse of(User user, List<String> hobbies, String mainPhoto,
        List<String> photos,
        List<String> lifeStyles, WeightValue weightValue) {
        return UserInfoResponse.builder()
            .userId(user.getId())
            .nickname(user.getNickname())
            .birth(user.getBirth())
            .age(user.getAge())
            .maxDistance(user.getMaxDistance())
            .minDistance(user.getMinDistance())
            .mainPhoto(mainPhoto)
            .photos(photos)
            .hobbies(hobbies)
            .lifeStyles(lifeStyles)
//            .incomeWeight(weightValue.getIncome())
            .lifeStyleWeight(weightValue.getLifeStyle())
            .hobbyWeight(weightValue.getHobby())
            .appearanceWeight(weightValue.getAppearance())
            .mbtiWeight(weightValue.getMbti())
            .phoneNum(user.getPhoneNum())
            .gender(user.getGender())
            .mbti(user.getMbti())
//            .income(user.getIncome())
            .education(user.getEducation())
            .school(user.getSchool())
            .career(user.getCareer())
            .hopeMinAge(user.getHopeMinAge())
            .hopeMaxAge(user.getHopeMaxAge())
            .heart(user.getHeart())
            .appearanceScore(user.getEvaluatedScore())
            .isSmoking(user.getIsSmoking())
            .isDrinking(user.getIsDrinking())
            .buddy(user.getBuddy())
            .religion(user.getReligion())
            .ideology(user.getIdeology())
            .contactFrequency(user.getContactFrequency())
            .conflictResolution(user.getConflictResolution())
            .height(user.getHeight())
            .bodyType(user.getBodyType())
            .build();
    }
}
