package ru.yandex.practicum.filmorate.exceptions;

public class FilmAlreadyExistsException extends RuntimeException{
    public FilmAlreadyExistsException(String name) {
        super(String.format("Film with id: \"%s\" already exists", name));
    }
}
