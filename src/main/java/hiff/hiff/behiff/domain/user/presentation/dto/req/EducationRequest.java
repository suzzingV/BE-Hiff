package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.global.validation.annotation.ValidEducation;
import lombok.Getter;

@Getter
public class EducationRequest {

    @ValidEducation
    private Education education;

    private String school;
}
