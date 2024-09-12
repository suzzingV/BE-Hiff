package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import hiff.hiff.behiff.global.validation.annotation.ValidEducation;
import hiff.hiff.behiff.global.validation.annotation.ValidGender;
import hiff.hiff.behiff.global.validation.annotation.ValidMbti;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.List;

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

    private Long careerId;

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
}
