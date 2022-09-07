package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.Birthday;
import ru.yandex.practicum.filmorate.validator.Login;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    @NonNull
    @Email
    private String email;
    @NonNull
    @Login
    private String login;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"dd.MM.yyyy"})
    @Birthday
    private LocalDate birthday;
}
