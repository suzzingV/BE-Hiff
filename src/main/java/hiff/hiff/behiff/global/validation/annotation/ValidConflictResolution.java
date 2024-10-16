package hiff.hiff.behiff.global.validation.annotation;

import hiff.hiff.behiff.global.validation.validator.BuddyValidator;
import hiff.hiff.behiff.global.validation.validator.ConflictResolutionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ConflictResolutionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidConflictResolution {

    String message() default "연인과의 갈등 해결이 유효하지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
