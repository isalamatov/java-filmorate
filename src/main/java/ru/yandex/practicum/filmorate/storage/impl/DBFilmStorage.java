package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
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
import java.util.ArrayList;
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
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rn) -> makeFilm(rs), id);
    }

    @Override
    public Film add(Film film) {
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
        jdbcTemplate.batchUpdate(sqlGenre, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = film.getGenres().get(i);
                ps.setInt(1, film.getId());
                ps.setInt(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return film.getGenres().size();
            }
        });
        return film;
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
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        String sqlCleanLikes = "DELETE FROM \"like\" WHERE \"film_id\" = ? ";
        jdbcTemplate.update(sqlCleanLikes, film.getId());
        String sqlQueryLikes = "MERGE INTO \"like\" VALUES (?, ?)";
        List<Integer> likedBy = new ArrayList<>(film.getLikedBy());
        if (likedBy.size() > 0) {
            jdbcTemplate.batchUpdate(sqlQueryLikes, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Integer liked = likedBy.get(i);
                    ps.setInt(1, film.getId());
                    ps.setInt(2, liked);
                }

                @Override
                public int getBatchSize() {
                    return film.getGenres().size();
                }
            });
        }
        String sqlCleanMPA = "DELETE FROM \"film_rating\" WHERE \"film_id\" = ? ";
        jdbcTemplate.update(sqlCleanMPA, film.getId());
        String sqlMPA = "MERGE INTO \"film_rating\" VALUES (?, ?)";
        jdbcTemplate.update(sqlMPA, film.getId(), film.getMpa().getId());
        String sqlCleanGenre = "DELETE FROM \"film_genre\" WHERE \"film_id\" = ? ";
        jdbcTemplate.update(sqlCleanGenre, film.getId());
        String sqlGenre = "MERGE INTO \"film_genre\" VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sqlGenre, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = film.getGenres().get(i);
                ps.setInt(1, film.getId());
                ps.setInt(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return film.getGenres().size();
            }
        });
    }

    @Override
    public void remove(Film film) {
        String sqlQuery = "DELETE FROM \"film\" WHERE \"film_id\" = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public boolean checkFilm(Integer id) {
        String sqlQuery = "SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END AS result FROM \"film\" WHERE \"film_id\" = ?";
        String result = jdbcTemplate.query(sqlQuery,
                (rs, rn) -> rs.getString("result"),
                id).get(0);
        return Boolean.parseBoolean(result);
    }

    @Override
    public boolean checkFilm(Film film) {
        String sqlQuery = "SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END AS result FROM \"film\" WHERE \"name\" = ?";
        String result = jdbcTemplate.query(sqlQuery,
                (rs, rn) -> rs.getString("result"),
                film.getId()).get(0);
        return Boolean.parseBoolean(result);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.\"film_id\"," +
                " f.\"name\"," +
                "f.\"description\", " +
                "f.\"release_date\", " +
                "f.\"duration\"," +
                "f.\"rate\"," +
                "f.\"rating_id\"," +
                "COUNT(l.\"user_id\") AS likes " +
                "FROM \"film\" AS f " +
                "LEFT JOIN \"like\" AS l ON f.\"film_id\" = l.\"film_id\"" +
                "GROUP BY f.\"film_id\" " +
                "ORDER BY likes DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, (rs, rn) -> makeFilm(rs), count);
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
