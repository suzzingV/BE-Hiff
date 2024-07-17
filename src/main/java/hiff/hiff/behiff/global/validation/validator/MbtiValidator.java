package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import hiff.hiff.behiff.global.validation.annotation.ValidMbti;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MbtiValidator implements ConstraintValidator<ValidMbti, Mbti> {

    @Override
    public boolean isValid(Mbti value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Mbti.ALL_VALUES.contains(value);
    }
}
