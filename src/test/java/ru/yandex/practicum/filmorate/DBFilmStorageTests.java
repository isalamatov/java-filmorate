package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.DBFilmStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DBFilmStorageTests {
	private final DBFilmStorage filmStorage;
	
	@Test
	@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void dbGet() {
		Optional<Film> film = Optional.ofNullable(filmStorage.get(1));
		assertThat(film)
				.isPresent()
				.hasValueSatisfying(x ->
						assertThat(x).hasFieldOrPropertyWithValue("name", "Melancholia")
				);
	}
	
	@Test
	@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void dbAdd() {
		Film film = filmStorage.get(1);
		film.setName("New film");
		filmStorage.add(film);
		assertThat(filmStorage.getAll())
				.hasSize(3)
				.hasOnlyElementsOfType(Film.class)
				.contains(film);
	}
	@Test
	@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void dbGetAll() {
		List<Film> films = filmStorage.getAll();
		assertThat(films)
				.hasSize(2)
				.hasOnlyElementsOfType(Film.class)
				.contains(filmStorage.get(1))
				.contains(filmStorage.get(2));
	}

	@Test
	@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void dbUpdate() {
		Film film = filmStorage.get(1);
		film.setName("Barbara Streizand");
		filmStorage.update(film);
		Film filmUpdated = filmStorage.get(1);
		assertThat(filmUpdated)
				.hasFieldOrPropertyWithValue("name", "Barbara Streizand")
				.hasFieldOrPropertyWithValue("id",1);
	}

	@Test
	@Sql(value = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = "/sql/sample_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void dbRemove() {
		Film film = filmStorage.get(1);
		filmStorage.remove(film);
		assertThatThrownBy(()->filmStorage.get(1)).hasMessage( "Incorrect result size: expected 1, actual 0");
	}
}