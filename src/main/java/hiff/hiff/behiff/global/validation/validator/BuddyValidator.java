package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.profile.domain.enums.Buddy;
import hiff.hiff.behiff.global.validation.annotation.ValidBuddy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BuddyValidator implements ConstraintValidator<ValidBuddy, Buddy> {

    @Override
    public boolean isValid(Buddy value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Buddy.ALL_VALUES.contains(value);
    }
}
