package org.beetola.app.validation.validator;

import org.beetola.app.validation.annotation.DecimalSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class DecimalSizeValidator implements ConstraintValidator<DecimalSize, BigDecimal> {

    private int maxPrecision;
    private int maxScale;

    @Override
    public void initialize(DecimalSize constraintAnnotation) {
        maxPrecision = constraintAnnotation.maxPrecision();
        maxScale = constraintAnnotation.maxScale();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value.precision() <= maxPrecision && value.scale() <= maxScale;
    }

}
