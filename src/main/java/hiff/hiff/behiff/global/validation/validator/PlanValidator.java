package hiff.hiff.behiff.global.validation.validator;

import hiff.hiff.behiff.domain.plan.domain.enums.Plan;
import hiff.hiff.behiff.global.validation.annotation.ValidPlan;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlanValidator implements ConstraintValidator<ValidPlan, Plan> {

    @Override
    public boolean isValid(Plan value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Plan.ALL_VALUES.contains(value);
    }
}
