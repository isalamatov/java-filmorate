package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.DBMpaStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DBMpaStorageTests {
    private final DBMpaStorage mpaStorage;

    @Test
    @Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dbGet() {
        Optional<Mpa> mpa = Optional.ofNullable(mpaStorage.getMpa(1));
        assertThat(mpa)
                .isPresent()
                .hasValueSatisfying(x ->
                        assertThat(x).hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    @Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dbGetAll() {
        List<Mpa> mpas = mpaStorage.getAllMpa();
        assertThat(mpas)
                .hasSize(5)
                .hasOnlyElementsOfType(Mpa.class)
                .contains(mpaStorage.getMpa(1))
                .contains(mpaStorage.getMpa(2));
    }

}