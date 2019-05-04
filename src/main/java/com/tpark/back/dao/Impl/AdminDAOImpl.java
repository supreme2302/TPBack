package com.tpark.back.dao.Impl;
import com.tpark.back.dao.AdminDAO;
import com.tpark.back.mapper.AdminMapper;
import com.tpark.back.model.dto.AdminDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminDAOImpl implements AdminDAO {

    private final JdbcTemplate jdbc;

    private final AdminMapper adminMapper;
    private final SchoolIDDAO schoolIDDAO;

    @Autowired
    public AdminDAOImpl(JdbcTemplate jdbc,
                        SchoolIDDAO schoolIDDAO,
                        AdminMapper adminMapper) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
        this.adminMapper = adminMapper;
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
    public Integer addAdmin(AdminDTO adminDTO) {
        final String sql = "INSERT INTO admin(email, password) VALUES (?, ?) RETURNING id";
        return jdbc.queryForObject(sql, Integer.class, adminDTO.getEmail(), adminDTO.getPassword());
    }

    @Override
    public void addNewAdmin(String toString, AdminDTO adminDTO) {
        Integer school_id = schoolIDDAO.getSchoolId(toString);
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

    @Override
    public List<AdminDTO> getSchoolAdmins(AdminDTO user) {
        try {
            final String SQL = "SELECT * FROM admin WHERE school_id = ?;";
            return jdbc.query(SQL, adminMapper, user.getSchoolId());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void changePassword(String email, String password) {
        final String sql = "UPDATE admin SET password = ? WHERE lower(admin.email) = lower(?);";
        jdbc.update(sql, password, email);
    }

//    public static class AdminMapper implements RowMapper<AdminDTO> {
//        @Override
//        public AdminDTO mapRow(ResultSet resultSet, int i) throws SQLException {
//            AdminDTO adminDTO = new AdminDTO();
//            adminDTO.setEmail(resultSet.getString("email"));
//            adminDTO.setPassword(resultSet.getString("password"));
//            adminDTO.setId(resultSet.getInt("id"));
//            return adminDTO;
//        }
//    }
}
