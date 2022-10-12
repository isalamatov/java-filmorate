package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Comparator;
import java.util.List;

@Repository
@Primary
public class DBGenreStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DBGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Genre> getAllGenres() {
        String sqlGetGenres = "SELECT \"genre_id\",\"name\" FROM \"genre\"";
        List<Genre> result = jdbcTemplate.query(sqlGetGenres,
                (rs, rn) -> (new Genre(rs.getInt("genre_id"), rs.getString("name"))));
        result.sort(Comparator.comparingInt(Genre::getId));
        return result;
    }

    public Genre getGenre(Integer id) {
        String sqlQueryGenre = "SELECT \"genre_id\",\"name\" FROM \"genre\" WHERE \"genre_id\" = ?";
        return jdbcTemplate.queryForObject(sqlQueryGenre,
                (rs, rn) -> (new Genre(id, rs.getString("name"))), id);
    }
}
