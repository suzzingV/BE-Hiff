package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.user.domain.enums.BodyType;
import hiff.hiff.behiff.domain.user.domain.enums.Company;
import hiff.hiff.behiff.global.validation.annotation.ValidBodyType;
import hiff.hiff.behiff.global.validation.annotation.ValidCompany;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyValidator implements ConstraintValidator<ValidCompany, Company> {

    @Override
    public boolean isValid(Company value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Company.ALL_VALUES.contains(value);
    }
}
