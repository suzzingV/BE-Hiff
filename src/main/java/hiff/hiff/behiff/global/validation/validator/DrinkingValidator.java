package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.user.domain.enums.Drinking;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.global.validation.annotation.ValidDrinking;
import hiff.hiff.behiff.global.validation.annotation.ValidGender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DrinkingValidator implements ConstraintValidator<ValidDrinking, Drinking> {

    @Override
    public boolean isValid(Drinking value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Drinking.ALL_VALUES.contains(value);
    }
}
