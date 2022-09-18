package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();
    Film get(Integer id);
    void add(Film film);

    void update(Film film);

    void remove(Film film);
}
