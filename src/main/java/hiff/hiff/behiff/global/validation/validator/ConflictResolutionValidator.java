package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.user.domain.enums.Buddy;
import hiff.hiff.behiff.domain.user.domain.enums.ConflictResolution;
import hiff.hiff.behiff.global.validation.annotation.ValidBuddy;
import hiff.hiff.behiff.global.validation.annotation.ValidConflictResolution;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConflictResolutionValidator implements ConstraintValidator<ValidConflictResolution, ConflictResolution> {

    @Override
    public boolean isValid(ConflictResolution value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return ConflictResolution.ALL_VALUES.contains(value);
    }
}
