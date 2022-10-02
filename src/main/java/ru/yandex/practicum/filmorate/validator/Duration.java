package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DurationValidator.class)

public @interface Duration {
    String message() default "Duration should be positive 3-digits number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
