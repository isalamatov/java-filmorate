package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class DescriptionValidator implements ConstraintValidator<Description, String> {
    public boolean isValid(String str, ConstraintValidatorContext cxt) {
        log.debug("Validation result of field value {} : {}", str, str.length() < 200);
        return str.length() < 200;
    }
}
