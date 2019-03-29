package com.tpark.back.dao.Impl;
import com.tpark.back.dao.AdminDAO;
import com.tpark.back.model.dto.AdminDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AdminDAOImpl implements AdminDAO {

    private final JdbcTemplate jdbc;

    private final static AdminMapper adminMapper = new AdminMapper();
    private final SchoolIDDAO schoolIDDAO;

    @Autowired
    public AdminDAOImpl(JdbcTemplate jdbc, SchoolIDDAO schoolIDDAO) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
    }

    @Override
    public AdminDTO getAdminByEmail(String email) {
        final String sql = "SELECT * FROM admin WHERE lower(email) = lower(?)";
        try {
            return jdbc.queryForObject(sql, adminMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void addAdmin(AdminDTO adminDTO) {
        final String sql = "INSERT INTO admin(email, password) VALUES (?, ?)";
        jdbc.update(sql, adminDTO.getEmail(), adminDTO.getPassword());
    }

    @Override
    public void addNewAdmin(String toString, AdminDTO adminDTO) {
        Integer school_id = schoolIDDAO.GetSchoolId(toString);
        final String sql = "INSERT INTO admin(email, password, school_id) VALUES (?, ?, ?)";
        jdbc.update(sql, adminDTO.getEmail(), adminDTO.getPassword(), school_id) ;

    }

    @Override
    public Integer getAdminIdByEmail(String email) {
        try {
            return jdbc.queryForObject("SELECT id FROM admin WHERE lower(email) = lower(?)", Integer.class, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    public static class AdminMapper implements RowMapper<AdminDTO> {
        @Override
        public AdminDTO mapRow(ResultSet resultSet, int i) throws SQLException {
            AdminDTO adminDTO = new AdminDTO();
            adminDTO.setEmail(resultSet.getString("email"));
            adminDTO.setPassword(resultSet.getString("password"));
            adminDTO.setId(resultSet.getInt("id"));
            return adminDTO;
        }
    }

    public void changePassword(String email, String password) {
        final String sql = "UPDATE admin SET password = ? WHERE lower(admin.email) = lower(?);";
        jdbc.update(sql, password, email);
    }
}
