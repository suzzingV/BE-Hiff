package hiff.hiff.behiff.domain.user.presentation.dto.res;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Income;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MyInfoResponse {

    private Long userId;

    private String email;

    private String nickname;

    private LocalDate birth;

    private Integer age;

    private Integer maxDistance;

    private Integer minDistance;

    private List<String> photos;

    private List<String> hobbies;

    private List<String> beliefs;

    private Integer incomeWeight;

    private Integer appearanceWeight;

    private Integer hobbyWeight;

    private Integer beliefWeight;

    private Integer mbtiWeight;

    private String phoneNum;

    private Gender gender;

    private Mbti mbti;

    private Income income;

    private String addr;

    private Education education;

    private String school;

    private String job;

    private Integer hopeMinAge;

    private Integer hopeMaxAge;

    private Integer heart;

    private Double appearanceScore;

    public static MyInfoResponse of(User user, List<String> hobbies, List<String> photos, List<String> beliefs, WeightValue weightValue) {
        return MyInfoResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .age(user.getAge())
                .maxDistance(user.getMaxDistance())
                .minDistance(user.getMinDistance())
                .photos(photos)
                .hobbies(hobbies)
                .beliefs(beliefs)
                .incomeWeight(weightValue.getIncome())
                .beliefWeight(weightValue.getBelief())
                .hobbyWeight(weightValue.getHobby())
                .appearanceWeight(weightValue.getAppearance())
                .mbtiWeight(weightValue.getMbti())
                .phoneNum(user.getPhoneNum())
                .gender(user.getGender())
                .mbti(user.getMbti())
                .income(user.getIncome())
                .addr(user.getAddr())
                .education(user.getEducation())
                .school(user.getSchool())
                .job(user.getJob())
                .hopeMinAge(user.getHopeMinAge())
                .hopeMaxAge(user.getHopeMaxAge())
                .heart(user.getHeart())
                .appearanceScore(user.getAppearanceScore())
                .build();
    }
}
