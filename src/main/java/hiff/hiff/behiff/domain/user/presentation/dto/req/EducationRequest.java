package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.global.validation.annotation.ValidEducation;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EducationRequest {

    @ValidEducation
    @NotNull
    private Education education;
}
