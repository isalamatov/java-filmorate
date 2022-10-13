package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private UserStorage userStorage;
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll() {
        log.trace("Get all films request received.");
        return filmStorage.getAll();
    }

    public Film get(Integer id) {
        log.trace("Get film request received: {}", id);
        if (!filmStorage.checkFilm(id)) {
            throw new FilmNotFoundException(id);
        }
        return filmStorage.get(id);
    }

    public Film update(Film film) {
        log.trace("Update film request received with data: {}", film);
        if (!filmStorage.checkFilm(film.getId())) {
            throw new FilmNotFoundException(film.getId());
        }
        filmStorage.update(film);
        return filmStorage.get(film.getId());
    }

    public Film add(Film film) {
        log.trace("Add film request received with data: {}", film);
        if (filmStorage.checkFilm(film)) {
            throw new FilmAlreadyExistsException(film.getName());
        }
        return filmStorage.add(film);
    }

    public void like(Integer filmId, Integer userID) {
        log.trace("Like film request received with data: {}", filmId, userID);

        Film film = filmStorage.get(filmId);
        User user = userStorage.get(userID);
        if (user.getLikedFilms() == null) {
            user.setLikedFilms(new HashSet<>());
        }
        if (film.getLikedBy() == null) {
            film.setLikedBy(new HashSet<>());
        }
        user.getLikedFilms().add(filmId);
        userStorage.update(user);
        film.getLikedBy().add(userID);
        filmStorage.update(film);
    }

    public void deleteLike(Integer filmId, Integer userID) {
        log.trace("Delete like request received with data: {}", filmId, userID);
        if (!filmStorage.checkFilm(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
        if (!userStorage.checkUser(userID)) {
            throw new UserNotFoundException(userID);
        }
        Film film = filmStorage.get(filmId);
        User user = userStorage.get(userID);
        if (user.getLikedFilms().contains(filmId)) {
            user.getLikedFilms().remove(filmId);
            userStorage.update(user);
        } else {
            throw new UserDoesnotLikeThatMovieException(filmId, userID);
        }
        if (film.getLikedBy().contains(userID)) {
            film.getLikedBy().remove(userID);
            filmStorage.update(film);
        } else {
            throw new UserDoesnotLikeThatMovieException(filmId, userID);
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        log.trace("Get popular films request received, list size: {}", count);
        if (count < 0) {
            throw new ValidationException("Count must be positive number");
        }
        return filmStorage.getPopularFilms(count);
    }
}
