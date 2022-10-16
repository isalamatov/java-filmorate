package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film get(Integer id);

    Film add(Film film);

    void update(Film film);

    void remove(Film film);

    boolean checkFilm(Integer id);

    boolean checkFilm(Film film);

    List<Film> getPopularFilms(Integer count);
}
