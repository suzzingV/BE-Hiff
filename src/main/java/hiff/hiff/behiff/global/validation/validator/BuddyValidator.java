package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.user.domain.enums.Buddy;
import hiff.hiff.behiff.domain.user.domain.enums.Drinking;
import hiff.hiff.behiff.global.validation.annotation.ValidBuddy;
import hiff.hiff.behiff.global.validation.annotation.ValidDrinking;
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
