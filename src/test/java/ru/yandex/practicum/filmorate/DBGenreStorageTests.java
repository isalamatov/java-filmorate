package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.DBGenreStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DBGenreStorageTests {
    private final DBGenreStorage genreStorage;

    @Test
    @Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dbGet() {
        Optional<Genre> genre = Optional.ofNullable(genreStorage.getGenre(1));
        assertThat(genre)
                .isPresent()
                .hasValueSatisfying(x ->
                        assertThat(x).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    @Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void dbGetAll() {
        List<Genre> genres = genreStorage.getAllGenres();
        assertThat(genres)
                .hasSize(6)
                .hasOnlyElementsOfType(Genre.class)
                .contains(genreStorage.getGenre(1))
                .contains(genreStorage.getGenre(2));
    }
}