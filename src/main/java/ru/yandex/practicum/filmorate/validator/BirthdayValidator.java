package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Slf4j
public class BirthdayValidator implements ConstraintValidator<Birthday, LocalDate> {

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = releaseDate.isBefore(LocalDate.now());
        log.debug("Validation result of field value {} : {}", releaseDate, result);
        return result;
    }
}
