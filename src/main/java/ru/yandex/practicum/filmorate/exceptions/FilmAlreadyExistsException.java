package ru.yandex.practicum.filmorate.exceptions;

public class FilmAlreadyExistsException extends RuntimeException{
    public FilmAlreadyExistsException(Integer id) {
        super(String.format("Film with id: \"%d\" already exists", id));
    }
}
