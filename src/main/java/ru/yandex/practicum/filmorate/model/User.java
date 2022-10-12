package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.Birthday;
import ru.yandex.practicum.filmorate.validator.Login;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Integer id;
    @NonNull
    @Email
    private String email;
    @NonNull
    @Login
    private String login;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"dd.MM.yyyy"})
    @Birthday
    private LocalDate birthday;
    private Set<Integer> friendsId;
    private Set<Integer> likedFilms;

    public User(Integer id, @NonNull String email, @NonNull String login, String name, LocalDate birthday, Set<Integer> friendsId, Set<Integer> likedFilms) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        if (friendsId == null) {
            this.friendsId = new HashSet<>();
        } else {
            this.friendsId = friendsId;
        }
        if (likedFilms == null) {
            this.likedFilms = new HashSet<>();
        } else {
            this.likedFilms = likedFilms;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getId() != null ? !getId().equals(user.getId()) : user.getId() != null) return false;
        if (!getEmail().equals(user.getEmail())) return false;
        if (!getLogin().equals(user.getLogin())) return false;
        if (getName() != null ? !getName().equals(user.getName()) : user.getName() != null) return false;
        if (getBirthday() != null ? !getBirthday().equals(user.getBirthday()) : user.getBirthday() != null)
            return false;
        if (getFriendsId() != null ? !getFriendsId().equals(user.getFriendsId()) : user.getFriendsId() != null)
            return false;
        return getLikedFilms() != null ? getLikedFilms().equals(user.getLikedFilms()) : user.getLikedFilms() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getLogin().hashCode();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getBirthday() != null ? getBirthday().hashCode() : 0);
        result = 31 * result + (getFriendsId() != null ? getFriendsId().hashCode() : 0);
        result = 31 * result + (getLikedFilms() != null ? getLikedFilms().hashCode() : 0);
        return result;
    }
}
