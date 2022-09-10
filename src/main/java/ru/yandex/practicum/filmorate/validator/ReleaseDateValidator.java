package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Slf4j
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        boolean result = releaseDate.isAfter(minDate);
        log.debug("Validation result of field value {} : {}", releaseDate, result);
        return releaseDate.isAfter(minDate);
    }
}
