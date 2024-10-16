package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.user.domain.enums.Buddy;
import hiff.hiff.behiff.domain.user.domain.enums.ContactFrequency;
import hiff.hiff.behiff.global.validation.annotation.ValidBuddy;
import hiff.hiff.behiff.global.validation.annotation.ValidContactFrequency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ContactFrequencyValidator implements ConstraintValidator<ValidContactFrequency, ContactFrequency> {

    @Override
    public boolean isValid(ContactFrequency value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return ContactFrequency.ALL_VALUES.contains(value);
    }
}
