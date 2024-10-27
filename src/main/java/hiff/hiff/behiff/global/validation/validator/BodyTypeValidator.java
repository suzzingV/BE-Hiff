package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.profile.domain.enums.BodyType;
import hiff.hiff.behiff.global.validation.annotation.ValidBodyType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BodyTypeValidator implements ConstraintValidator<ValidBodyType, BodyType> {

    @Override
    public boolean isValid(BodyType value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return BodyType.ALL_VALUES.contains(value);
    }
}
