package ru.yandex.practicum.filmorate.exceptions;

public class UsersAlreadyNotFriendsException extends RuntimeException{
    public UsersAlreadyNotFriendsException(Integer id1, Integer id2) {
        super(String.format("User with id \"%d\" is not a friend of user with id \"%d\".", id1, id2));
    }
}
