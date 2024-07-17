package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.global.validation.annotation.ValidSocialType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SocialTypeValidator implements ConstraintValidator<ValidSocialType, SocialType> {

    @Override
    public boolean isValid(SocialType value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return SocialType.ALL_VALUES.contains(value);
    }
}
