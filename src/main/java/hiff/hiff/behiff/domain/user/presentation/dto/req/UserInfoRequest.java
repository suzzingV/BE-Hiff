package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import hiff.hiff.behiff.global.validation.annotation.ValidGender;
import hiff.hiff.behiff.global.validation.annotation.ValidMbti;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;

@Getter
public class UserInfoRequest {

    @NotNull
    @Min(1900)
    @Max(2024)
    private Integer birthYear;

    @NotNull
    @Min(1)
    @Max(12)
    private Integer birthMonth;

    @NotNull
    @Min(1)
    @Max(31)
    private Integer birthDay;

//    private Long careerId;

    @ValidGender
    @NotNull
    private Gender gender;

    private List<Long> originHobbies;

    private List<Long> originLifeStyles;

    @ValidMbti
    @NotNull
    private Mbti mbti;

    @Size(min = 1, max = 8)
    @NotEmpty
    private String nickname;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer appearanceWV;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer hobbyWV;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer lifeStyleWV;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer mbtiWV;

    @Min(19)
    @Max(50)
    @NotNull
    private Integer minAge;

    @Min(19)
    @Max(50)
    @NotNull
    private Integer maxAge;

    @Min(0)
    @Max(700)
    @NotNull
    private Integer maxDistance;

    @Min(0)
    @Max(700)
    @NotNull
    private Integer minDistance;
}
