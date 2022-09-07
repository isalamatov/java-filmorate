package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LoginValidator.class)
public @interface Login {
    String message() default "Login should be without space characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
