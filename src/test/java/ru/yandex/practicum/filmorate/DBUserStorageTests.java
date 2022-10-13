package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.DBUserStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DBUserStorageTests {
    private final DBUserStorage userStorage;

    @Test
    @Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dbGet() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.get(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Jane Doe")
                );
    }

    @Test
    @Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dbGetAll() {
        List<User> users = userStorage.getAll();
        assertThat(users)
                .hasSize(2)
                .hasOnlyElementsOfType(User.class)
                .contains(userStorage.get(1))
                .contains(userStorage.get(2));
    }

    @Test
    @Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dbAdd() {
        User user = userStorage.get(1);
		user.setName("New user");
		user.setEmail("new@mail.com");
		userStorage.add(user);
        assertThat(userStorage.getAll())
                .hasSize(3)
                .hasOnlyElementsOfType(User.class)
                .contains(user);
    }

    @Test
    @Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dbUserUpdate() {
        User user = userStorage.get(1);
        user.setName("Barbara Streizand");
        userStorage.update(user);
        User userUpdated = userStorage.get(1);
        assertThat(userUpdated)
                .hasFieldOrPropertyWithValue("name", "Barbara Streizand")
                .hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dbUserRemove() {
        User user = userStorage.get(1);
        userStorage.remove(user);
        assertThatThrownBy(() -> userStorage.get(1)).hasMessage("Incorrect result size: expected 1, actual 0");
    }
}