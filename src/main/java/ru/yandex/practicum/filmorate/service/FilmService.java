package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesnotLikeThatMovieException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
        return filmStorage.get(id);
    }

    public void update(Film film) {
        log.trace("Update film request received with data: {}", film);
        filmStorage.update(film);
    }

    public void add(Film film) {
        log.trace("Add film request received with data: {}", film);
        filmStorage.add(film);
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
        return filmStorage.getAll().stream()
                .peek(x -> {
                    if (x.getLikedBy() == null) x.setLikedBy(new HashSet<>());
                })
                .sorted(Comparator.comparingInt(o -> -o.getLikedBy().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
