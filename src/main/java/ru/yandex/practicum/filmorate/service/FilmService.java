package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesnotLikeThatMovieException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private UserStorage userStorage;
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void like(Integer filmId, Integer userID) {
        if (userStorage.get(userID).getLikedFilms() == null) {
            userStorage.get(userID).setLikedFilms(new HashSet<>());
        }
        if (filmStorage.get(filmId).getLikedBy() == null) {
            filmStorage.get(filmId).setLikedBy(new HashSet<>());
        }
        userStorage.get(userID).getLikedFilms().add(filmId);
        filmStorage.get(filmId).getLikedBy().add(userID);
    }

    public void deleteLike(Integer filmId, Integer userID) {
        if (userStorage.get(userID).getLikedFilms().contains(filmId)) {
            userStorage.get(userID).getLikedFilms().remove(filmId);
        } else {
            throw new UserDoesnotLikeThatMovieException(filmId, userID);
        }
        if (filmStorage.get(filmId).getLikedBy().contains(userID)) {
            filmStorage.get(filmId).getLikedBy().remove(userID);
        } else {
            throw new UserDoesnotLikeThatMovieException(filmId, userID);
        }
    }

    public List<Film> getPopularFilms(Integer count) {

        return filmStorage.getAll().stream()
                .peek(x -> {
                    if (x.getLikedBy() == null) x.setLikedBy(new HashSet<>());
                })
                .sorted(Comparator.comparingInt(o -> -o.getLikedBy().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
