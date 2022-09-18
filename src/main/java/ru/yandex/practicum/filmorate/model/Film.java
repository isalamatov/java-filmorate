package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.Description;
import ru.yandex.practicum.filmorate.validator.Duration;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotBlank(message = "Name must not be blank.")
    private String name;
    @Description
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"dd.MM.yyyy"})
    @ReleaseDate
    private LocalDate releaseDate;
    @Digits(integer = 3, fraction = 0, message = "Duration must be 3 digits length")
    @Duration
    @Positive
    private Integer duration;
    private Set<Integer> likedBy = new HashSet<>();
}
