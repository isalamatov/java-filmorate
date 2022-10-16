package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
public class DBUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT \"user_id\"," +
                "\"email\"," +
                "\"login\", " +
                "\"name\", " +
                "\"birthday\" " +
                "FROM \"user\"";
        return jdbcTemplate.query(sqlQuery, (rs, rn) -> makeUser(rs));
    }

    @Override
    public User get(Integer id) {
        String sqlQuery = "SELECT \"user_id\"," +
                "\"email\"," +
                "\"login\", " +
                "\"name\", " +
                "\"birthday\" " +
                "FROM \"user\" " +
                "WHERE \"user_id\" = ? ";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rn) -> makeUser(rs), id);
    }

    @Override
    public void add(User user) {
        String sqlQuery = "INSERT INTO \"user\" (\"email\", \"login\", \"name\", \"birthday\")" +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday().format(DateTimeFormatter.ISO_DATE)));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void update(User user) {
        String sqlQuery = "UPDATE \"user\" SET" +
                "\"email\" = ?," +
                "\"login\" = ?, " +
                "\"name\" = ?, " +
                "\"birthday\" = ? " +
                "WHERE \"user_id\" = ? ";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        String sqlCleanAllFriends = "DELETE FROM \"friend\" WHERE \"user_id\"= ? ";
        jdbcTemplate.update(sqlCleanAllFriends, user.getId());
        String sqlQueryFriends = "MERGE INTO \"friend\" VALUES ( ?, ?, true)";
        List<Integer> friendsIds = new ArrayList<>(user.getFriendsId());
        jdbcTemplate.batchUpdate(sqlQueryFriends, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Integer friendsId = friendsIds.get(i);
                ps.setInt(1, user.getId());
                ps.setInt(2, friendsId);
            }

            @Override
            public int getBatchSize() {
                return friendsIds.size();
            }
        });
        String sqlCleanAllLikes = "DELETE FROM \"like\" WHERE \"user_id\" = ?";
        jdbcTemplate.update(sqlCleanAllLikes, user.getId());
        String sqlLikedFilms = "MERGE INTO \"like\" VALUES ( ?, ?)";
        List<Integer> likedFilms = new ArrayList<>(user.getLikedFilms());
        if (likedFilms.size() > 0) {
            jdbcTemplate.batchUpdate(sqlLikedFilms, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Integer liked = likedFilms.get(i);
                    ps.setInt(1, liked);
                    ps.setInt(2, user.getId());
                }

                @Override
                public int getBatchSize() {
                    return friendsIds.size();
                }
            });
        }
    }

    @Override
    public void remove(User user) {
        try {
            get(user.getId());
        } catch (UserNotFoundException exception) {
            throw exception;
        }
        String sqlQuery = "DELETE FROM \"user\" WHERE \"user_id\" = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public boolean checkUser(Integer id) {
        String sqlQuery = "SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END AS result FROM \"user\" WHERE \"user_id\" = ?";
        String result = jdbcTemplate.query(sqlQuery,
                (rs, rn) -> rs.getString("result"),
                id).get(0);
        return Boolean.parseBoolean(result);
    }

    @Override
    public boolean checkUser(User user) {
        String sqlQuery = "SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END AS result FROM \"user\" WHERE \"email\" = ?";
        String result = jdbcTemplate.query(sqlQuery,
                (rs, rn) -> rs.getString("result"),
                user.getEmail()).get(0);
        return Boolean.parseBoolean(result);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        String sqlQuery = "SELECT \"friend_id\" FROM \"friend\" WHERE \"user_id\" = ?";
        Set<Integer> friendsId = new HashSet<>(jdbcTemplate.query(sqlQuery, (rs1, rn) -> rs1.getInt("friend_id"), id));
        sqlQuery = "SELECT \"film_id\" FROM \"like\" WHERE \"user_id\" = ?";
        Set<Integer> likedFilms = new HashSet<>(jdbcTemplate.query(sqlQuery, (rs1, rn) -> rs1.getInt("film_id"), id));
        return new User(id, email, login, name, birthday, friendsId, likedFilms);
    }
}
