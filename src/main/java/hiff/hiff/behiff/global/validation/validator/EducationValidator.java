package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.user.domain.enums.Education;
import hiff.hiff.behiff.global.validation.annotation.ValidEducation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EducationValidator implements ConstraintValidator<ValidEducation, Education> {

    @Override
    public boolean isValid(Education value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Education.ALL_VALUES.contains(value);
    }
}
