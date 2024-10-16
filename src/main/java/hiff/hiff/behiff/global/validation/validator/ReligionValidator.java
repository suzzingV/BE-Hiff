package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.user.domain.enums.Buddy;
import hiff.hiff.behiff.domain.user.domain.enums.Religion;
import hiff.hiff.behiff.global.validation.annotation.ValidBuddy;
import hiff.hiff.behiff.global.validation.annotation.ValidReligion;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReligionValidator implements ConstraintValidator<ValidReligion, Religion> {

    @Override
    public boolean isValid(Religion value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Religion.ALL_VALUES.contains(value);
    }
}
