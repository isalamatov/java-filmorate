package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.SettableListenableFuture;
import ru.yandex.practicum.filmorate.exceptions.UsersAlreadyNotFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer mainId, Integer secondaryId) {
        if (userStorage.get(mainId).getFriendsId() == null) {
            userStorage.get(mainId).setFriendsId(new HashSet<>());
        }
        if (userStorage.get(secondaryId).getFriendsId() == null) {
            userStorage.get(secondaryId).setFriendsId(new HashSet<>());
        }
        userStorage.get(mainId).getFriendsId().add(secondaryId);
        userStorage.get(secondaryId).getFriendsId().add(mainId);
    }

    public void deleteFriend(Integer mainId, Integer secondaryId) {
        if (userStorage.get(mainId).getFriendsId().contains(secondaryId)) {
            userStorage.get(mainId).getFriendsId().remove(secondaryId);
        } else {
            throw new UsersAlreadyNotFriendsException(mainId, secondaryId);
        }
        if (userStorage.get(secondaryId).getFriendsId().contains(mainId)) {
            userStorage.get(secondaryId).getFriendsId().remove(mainId);
        } else {
            throw new UsersAlreadyNotFriendsException(secondaryId, mainId);
        }
    }

    public List<User> getFriends(Integer mainId) {
        return userStorage.get(mainId).getFriendsId().stream()
                .map(x -> userStorage.get(x))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer mainId, Integer secondaryId) {
        if (userStorage.get(mainId).getFriendsId() == null
                || userStorage.get(secondaryId).getFriendsId() == null) {
            return Collections.emptyList();
        }
        return userStorage.get(mainId).getFriendsId().stream()
                .filter(x -> userStorage.get(secondaryId).getFriendsId().contains(x))
                .map(x -> userStorage.get(x))
                .collect(Collectors.toList());
    }
}
