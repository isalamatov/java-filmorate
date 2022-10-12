package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;
    private UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.get(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        filmService.update(film);
        return filmService.get(film.getId());
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        filmService.add(film);
        return filmService.get(film.getId());
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable String id, @PathVariable String userId) {
        Integer userID = parseUserId(userId);
        Integer filmId = parseFilmId(id);
        filmService.like(filmId, userID);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable String id, @PathVariable String userId) {
        Integer userID = parseUserId(userId);
        Integer filmId = parseFilmId(id);
        filmService.deleteLike(filmId, userID);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        if (count < 0) {
            throw new ValidationException("Count must be positive number");
        }
        return filmService.getPopularFilms(count);
    }

    private Integer parseFilmId(String id) {
        Integer result;
        try {
            result = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    String.format("Film id must be valid integer number \"%s\" ", e.getMessage())
            );
        }
        if (filmService.getAll().stream().noneMatch(x -> x.getId() == result)) {
            throw new FilmNotFoundException(result);
        }
        return result;
    }

    private Integer parseUserId(String id) {
        Integer result;
        try {
            result = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    String.format("User id must be valid integer number \"%s\" ", e.getMessage())
            );
        }
        if (userService.getAll().stream().noneMatch(x -> x.getId() == result)) {
            throw new UserNotFoundException(result);
        }
        return result;
    }
}
