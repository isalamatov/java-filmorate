package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Primary
public class DBMpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpa(Integer id) {
        String sqlQueryMpa = "SELECT \"rating_id\",\"name\" FROM \"rating\" WHERE \"rating_id\" = ?";
        return jdbcTemplate.queryForObject(sqlQueryMpa, (rs, rn) -> (new Mpa(id, rs.getString("name"))), id);
    }

    public List<Mpa> getAllMpa() {
        String sqlQueryMpa = "SELECT \"rating_id\",\"name\" FROM \"rating\"";
        return jdbcTemplate.query(sqlQueryMpa,
                (rs, rn) -> (new Mpa(rs.getInt("rating_id"), rs.getString("name"))));
    }
}
