package ru.yandex.practicum.filmorate.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super(String.format("User with email \"%s\"", email));
    }
}
