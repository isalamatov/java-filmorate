package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    Integer lastId = 0;
    HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film get(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException(id);
        }
        return films.get(id);
    }

    @Override
    public void add(Film film) {
        if (films.containsValue(film)) {
            throw new FilmAlreadyExistsException(film.getId());
        }
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Cannot update film with null id");
        } else if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(film.getId());
        }
        films.put(film.getId(), film);
    }

    @Override
    public void remove(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Cannot delete film with null id");
        } else if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(film.getId());
        }
        films.remove(film.getId());
    }

    private Integer generateId() {
        return ++lastId;
    }
}
