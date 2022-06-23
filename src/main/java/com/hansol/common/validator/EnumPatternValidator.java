package com.hansol.common.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.hansol.common.annotation.EnumPattern;
import com.hansol.common.exception.InvalidValueException;
import com.hansol.common.payload.ResponseType;

public class EnumPatternValidator implements ConstraintValidator<EnumPattern, Enum<?>> {
    private Pattern pattern;

    @Override
    public void initialize(EnumPattern annotation) {
        try {
            String regex = String.join("|", annotation.enums());
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            throw new InvalidValueException(ResponseType.INVALID_ENUM_VALUE);
        }
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) return false;

        Matcher matcher = pattern.matcher(value.name());
        return matcher.matches();
    }
}