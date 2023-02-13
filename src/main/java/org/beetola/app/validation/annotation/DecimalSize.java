package org.beetola.app.validation.annotation;

import org.beetola.app.validation.validator.DecimalSizeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DecimalSizeValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalSize {

    String message() default "Number's precision or scale incorrect";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int maxPrecision();

    int maxScale();

}
