package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserStorage userStorage;

    private UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) {
        return userService.get(id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        userService.update(user);
        return user;
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        userService.add(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable String id, @PathVariable String friendId) {
        Integer mainId = parseId(id);
        Integer secondaryId = parseId(friendId);
        userService.addFriend(mainId, secondaryId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        Integer mainId = parseId(id);
        Integer secondaryId = parseId(friendId);
        userService.deleteFriend(mainId, secondaryId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable String id) {
        Integer mainId = parseId(id);
        return userService.getFriends(mainId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable String id, @PathVariable String otherId) {
        Integer mainId = parseId(id);
        Integer secondaryId = parseId(otherId);
        return userService.getCommonFriends(mainId, secondaryId);
    }

    private Integer parseId(String id) {
        Integer result;
        try {
            result = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    String.format("Id and friend id should be valid integer numbers \"%s\" ", e.getMessage())
            );
        }
        if (userService.getAll().stream().noneMatch(x -> x.getId() == result)) {
            throw new UserNotFoundException(result);
        }
        return result;
    }
}
