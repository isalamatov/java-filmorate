package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class DurationValidator implements ConstraintValidator<Duration, Integer> {
    @Override
    public boolean isValid(Integer duration, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = duration > 0;
        log.debug("Validation result of field value {} : {}", duration, result);
        return result;
    }
}
