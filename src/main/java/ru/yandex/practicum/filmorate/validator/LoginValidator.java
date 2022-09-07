package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class LoginValidator implements ConstraintValidator<Login, String> {

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = !str.contains(" ");
        log.debug("Validation result of field value {} : {}", str, result);
        return result;
    }
}
