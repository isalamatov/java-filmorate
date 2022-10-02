package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DescriptionValidator.class)
public @interface Description {
    String message() default "Description should be fit in 200 letters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
