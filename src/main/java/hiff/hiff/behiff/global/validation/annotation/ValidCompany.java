package hiff.hiff.behiff.global.validation.annotation;

import hiff.hiff.behiff.global.validation.validator.BodyTypeValidator;
import hiff.hiff.behiff.global.validation.validator.CompanyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CompanyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCompany {

    String message() default "회사가 유효하지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
