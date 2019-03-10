package com.tpark.back.dao.Impl;
import com.tpark.back.dao.UserDAO;
import com.tpark.back.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDAOImpl implements UserDAO {

    private final JdbcTemplate jdbc;
    private final static UserMapper userMapper = new UserMapper();

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public User getUserByEmail(String email) {
        final String sql = "SELECT * FROM main_user WHERE lower(email) = lower(?)";
        try {
            return jdbc.queryForObject(sql, userMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public void addUser(User user) {
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
        final String sql = "UPDATE main_user SET password = ? WHERE lower(main_user.email) = lower(?);";
        jdbc.update(sql, password, email);
    }
}
