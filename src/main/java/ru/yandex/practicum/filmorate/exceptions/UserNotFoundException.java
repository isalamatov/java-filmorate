package ru.yandex.practicum.filmorate.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer id) {
        super(String.format("User with id: \"%d\" not found", id));
    }
}
