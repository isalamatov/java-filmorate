package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class UserController {
    HashMap<Integer, User> users = new HashMap<>();
    private int idCounter = 0;

    @GetMapping("/users")
    public List<User> getAll() {
        log.trace("Get all users request received.");
        return new ArrayList<>(users.values());
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.trace("Update user request received with data: {}", user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("User with ID" + user.getId() + "doesn't exists.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @PostMapping("/users")
    public User add(@Valid @RequestBody User user) {
        log.trace("Add user request received with data: {}", user);
        Optional<Integer> id = Optional.ofNullable(user.getId());
        Optional<String> name = Optional.ofNullable(user.getName());
        if (id.isPresent() && users.containsKey(user.getId())) {
            throw new RuntimeException("Film with ID" + user.getId() + "already exists, use PUT method to update it.");
        }
        if (id.isEmpty()) {
            id = Optional.of(++idCounter);
        }
        if (name.isEmpty() || name.get().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id.get());
        users.put(user.getId(), user);
        return user;
    }

}
