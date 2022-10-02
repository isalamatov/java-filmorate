package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User get(Integer id);

    void add(User user);

    void update(User user);

    void remove(User user);
}
