package com.hansol.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import com.hansol.common.validator.EnumPatternValidator;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumPatternValidator.class)
public @interface EnumPattern {
    String[] enums();
    String message() default "선택 값을 확인하세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}