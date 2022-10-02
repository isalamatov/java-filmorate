package ru.yandex.practicum.filmorate.model;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;

        Film film = (Film) o;

        if (getName() != null ? !getName().equals(film.getName()) : film.getName() != null) return false;
        if (getDescription() != null ? !getDescription().equals(film.getDescription()) : film.getDescription() != null)
            return false;
        if (getReleaseDate() != null ? !getReleaseDate().equals(film.getReleaseDate()) : film.getReleaseDate() != null)
            return false;
        return getDuration() != null ? getDuration().equals(film.getDuration()) : film.getDuration() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getReleaseDate() != null ? getReleaseDate().hashCode() : 0);
        result = 31 * result + (getDuration() != null ? getDuration().hashCode() : 0);
        return result;
    }
}
