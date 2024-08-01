package hiff.hiff.behiff.domain.user.presentation.dto.res;

import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyInfoResponse {

    private Long userId;

    private String nickname;

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

    private Integer income;

    private String addr;

    private Education education;

    private String school;

    private String job;

    private Integer hopeMinAge;

    private Integer hopeMaxAge;
}
