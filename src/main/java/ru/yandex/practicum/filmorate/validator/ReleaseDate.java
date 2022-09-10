package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)

public @interface ReleaseDate {
    String message() default "Release date should be in format of pattern dd:MM:YYYY and be after 28:12:1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
