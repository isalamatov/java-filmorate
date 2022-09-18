package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Integer idCounter = 0;
    private FilmStorage filmStorage;
    private FilmService filmService;
    private UserStorage userStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public List<Film> getAll() {
        log.trace("Get all films request received.");
        return filmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.trace("Get film request received.");
        return filmStorage.get(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.trace("Update film request received with data: {}", film);
        filmStorage.update(film);
        return film;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.trace("Add film request received with data: {}", film);
        filmStorage.add(film);
        return film;
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
        if (filmStorage.getAll().stream().noneMatch(x -> x.getId() == result)) {
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
        if (userStorage.getAll().stream().noneMatch(x -> x.getId() == result)) {
            throw new UserNotFoundException(result);
        }
        return result;
    }
}
