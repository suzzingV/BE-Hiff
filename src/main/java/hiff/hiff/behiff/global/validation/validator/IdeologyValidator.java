package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.profile.domain.enums.Ideology;
import hiff.hiff.behiff.global.validation.annotation.ValidIdeology;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdeologyValidator implements ConstraintValidator<ValidIdeology, Ideology> {

    @Override
    public boolean isValid(Ideology value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Ideology.ALL_VALUES.contains(value);
    }
}
