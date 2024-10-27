package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.profile.domain.enums.Fashion;
import hiff.hiff.behiff.global.validation.annotation.ValidFashion;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FashionValidator implements ConstraintValidator<ValidFashion, Fashion> {

    @Override
    public boolean isValid(Fashion value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Fashion.ALL_VALUES.contains(value);
    }
}
