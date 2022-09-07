package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BirthdayValidator.class)

public @interface Birthday {
    String message() default "Birthday should be before actual moment.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
