package com.tpark.back.dao;
import com.tpark.back.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDAO {

    private final JdbcTemplate jdbc;
    private final static UserMapper userMapper = new UserMapper();

    @Autowired
    public UserDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public User getUser(String email) {
        final String sql = "SELECT * FROM main_user WHERE lower(email) = lower(?)";
        try {
            return jdbc.queryForObject(sql, userMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void createUser(User user) {
        final String sql = "INSERT INTO main_user(email, password) VALUES (?, ?)";
        jdbc.update(sql, user.getEmail(), user.getPassword());
    }

    public static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setId(resultSet.getInt("id"));
            return user;
        }
    }

    public void changePassword(String email, String password) {
        final String sql = "UPDATE main_user SET password = ? WHERE main_user.email = (?);";
        jdbc.update(sql, password, email);
    }
}
