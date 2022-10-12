package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Primary
public class DBFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DBFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.\"film_id\"," +
                " f.\"name\"," +
                "f.\"description\", " +
                "f.\"release_date\", " +
                "f.\"duration\"," +
                "f.\"rate\"," +
                "f.\"rating_id\"" +
                "FROM \"film\" AS f ";
        return jdbcTemplate.query(sqlQuery, (rs, rn) -> makeFilm(rs));
    }

    @Override
    public Film get(Integer id) {
        String sqlQuery = "SELECT f.\"film_id\"," +
                " f.\"name\"," +
                "f.\"description\", " +
                "f.\"release_date\", " +
                "f.\"duration\"," +
                "f.\"rate\"," +
                "f.\"rating_id\"" +
                "FROM \"film\" AS f " +
                "WHERE f.\"film_id\" = ? ";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, (rs, rn) -> makeFilm(rs), id);
        } catch (EmptyResultDataAccessException exception) {
            throw new FilmNotFoundException(id);
        }
    }

    @Override
    public void add(Film film) {
        String sqlQuery = "INSERT INTO \"film\" (\"name\", \"description\", \"release_date\", \"duration\", \"rate\", \"rating_id\")" +
                "VALUES (?, ?, ?, ?, ?, ?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate().format(DateTimeFormatter.ISO_DATE)));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        String sqlMPA = "MERGE INTO \"film_rating\" VALUES (?, ?)";
        jdbcTemplate.update(sqlMPA, film.getId(), film.getMpa().getId());
        String sqlGenre = "MERGE INTO \"film_genre\" VALUES (?, ?)";
        film.getGenres().forEach(genre ->
                jdbcTemplate.update(sqlGenre, film.getId(), genre.getId())
        );
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "UPDATE \"film\" SET " +
                "\"name\" = ?, " +
                "\"description\" = ?," +
                " \"release_date\" = ?," +
                " \"duration\" = ?," +
                " \"rate\" = ?," +
                "\"rating_id\" = ?" +
                "WHERE \"film_id\" = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()) == 0) {
            throw new FilmNotFoundException(film.getId());
        }
        String sqlCleanLikes = "DELETE FROM \"like\" WHERE \"film_id\" = ? ";
        jdbcTemplate.update(sqlCleanLikes, film.getId());
        film.getLikedBy().forEach(userId -> {
                    String sqlQuery1 = "MERGE INTO \"like\" VALUES (?, ?)";
                    jdbcTemplate.update(sqlQuery1, film.getId(), userId);
                }
        );
        String sqlCleanMPA = "DELETE FROM \"film_rating\" WHERE \"film_id\" = ? ";
        jdbcTemplate.update(sqlCleanMPA, film.getId());
        String sqlMPA = "MERGE INTO \"film_rating\" VALUES (?, ?)";
        jdbcTemplate.update(sqlMPA, film.getId(), film.getMpa().getId());
        String sqlCleanGenre = "DELETE FROM \"film_genre\" WHERE \"film_id\" = ? ";
        jdbcTemplate.update(sqlCleanGenre, film.getId());
        film.getGenres().forEach(genre -> {
                    String sqlGenre = "MERGE INTO \"film_genre\" VALUES (?, ?)";
                    jdbcTemplate.update(sqlGenre, film.getId(), genre.getId());
                }
        );
    }

    @Override
    public void remove(Film film) {
        try {
            get(film.getId());
        } catch (FilmNotFoundException exception) {
            throw exception;
        }
        String sqlQuery = "DELETE FROM \"film\" WHERE \"film_id\" = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer duration = rs.getInt("duration");
        Integer rate = rs.getInt("rate");
        Integer ratingId = rs.getInt("rating_id");
        String sqlQueryMPA = "SELECT \"name\" FROM \"rating\" WHERE \"rating_id\" = ?";
        Mpa mpa = jdbcTemplate.queryForObject(sqlQueryMPA,
                (rs1, rn) -> (new Mpa(ratingId, rs1.getString("name"))),
                ratingId);
        String sqlQueryGenre = "SELECT g.\"genre_id\",g.\"name\" FROM \"genre\" AS g WHERE g.\"genre_id\" " +
                "IN (SELECT f.\"genre_id\" FROM \"film_genre\" AS f WHERE f.\"film_id\" = ?)";
        List<Genre> genres = jdbcTemplate.query(sqlQueryGenre,
                (rs2, rn) -> (new Genre(rs2.getInt("genre_id"), rs2.getString("name"))),
                id);
        String sqlQueryLikes = "SELECT \"user_id\" FROM \"like\" WHERE \"film_id\" = ?";
        Set<Integer> likedBy = new HashSet<>(jdbcTemplate.query(sqlQueryLikes,
                (rs3, rn) -> rs3.getInt("user_id"),
                id));
        return new Film(id, name, description, releaseDate, duration, rate, mpa, genres, likedBy);
    }
}
