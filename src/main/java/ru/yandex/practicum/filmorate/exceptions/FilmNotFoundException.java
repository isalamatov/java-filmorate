package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(Integer id) {
        super(String.format("Film with id: \"%d\" not found", id));
    }
}
