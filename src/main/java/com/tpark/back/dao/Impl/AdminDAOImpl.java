package com.tpark.back.dao.Impl;
import com.tpark.back.dao.AdminDAO;
import com.tpark.back.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AdminDAOImpl implements AdminDAO {

    private final DataSource dataSource;

    private JdbcTemplate jdbc;

    private final static AdminMapper adminMapper = new AdminMapper();

    @Autowired
    public AdminDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        this.jdbc = new JdbcTemplate(this.dataSource);
    }

    @Override
    public Admin getAdminByEmail(String email) {
        final String sql = "SELECT * FROM admin WHERE lower(email) = lower(?)";
        try {
            return jdbc.queryForObject(sql, adminMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public void addAdmin(Admin admin) {
        final String sql = "INSERT INTO admin(email, password) VALUES (?, ?)";
        jdbc.update(sql, admin.getEmail(), admin.getPassword());
    }

    public static class AdminMapper implements RowMapper<Admin> {
        @Override
        public Admin mapRow(ResultSet resultSet, int i) throws SQLException {
            Admin admin = new Admin();
            admin.setEmail(resultSet.getString("email"));
            admin.setPassword(resultSet.getString("password"));
            admin.setId(resultSet.getInt("id"));
            return admin;
        }
    }

    public void changePassword(String email, String password) {
        final String sql = "UPDATE admin SET password = ? WHERE lower(admin.email) = lower(?);";
        jdbc.update(sql, password, email);
    }
}
