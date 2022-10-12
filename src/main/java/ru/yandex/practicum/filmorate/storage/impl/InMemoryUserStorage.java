package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    Integer lastId = 0;
    private HashMap<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        }
        return users.get(id);
    }

    @Override
    public void add(User user) {
        boolean duplicateEmail = users.values().stream()
                .anyMatch(x -> x.getEmail().equalsIgnoreCase(user.getEmail()));
        if (duplicateEmail) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Cannot update user with null id");
        } else if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void remove(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Cannot delete user with null id");
        } else if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
        users.remove(user.getId());
    }

    private Integer generateId() {
        return ++lastId;
    }
}
