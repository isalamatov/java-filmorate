package ru.yandex.practicum.filmorate.exceptions;

public class UserDoesnotLikeThatMovieException extends RuntimeException {
    public UserDoesnotLikeThatMovieException(Integer filmId, Integer userId) {
        super(String.format("User with id \"%d\" doesn't like movie with id \"%d\"", userId, filmId));
    }
}
