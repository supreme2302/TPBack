package com.tpark.back.dao.Impl;

import com.tpark.back.dao.SchoolDAO;
import com.tpark.back.model.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class SchoolDAOImpl implements SchoolDAO {
    private final JdbcTemplate jdbc;
    private final static SchoolMapper schoolMapper = new SchoolMapper();

    @Autowired
    public SchoolDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void createSchool(School school) {
        final String sql = "INSERT INTO school(school_name, device_id) VALUES (?, ?);";
        jdbc.update(sql, school.getName(), school.getDev_id());
    }

    @Override
    public School getSchoolByAdmin(String admin) {
        final String sql = "SELECT * FROM school JOIN admin ON admin.school_id = school.id AND lower(admin.email) = lower(?); ";
        return jdbc.queryForObject(sql, schoolMapper, admin);
    }

    @Override
    public School getSchoolByStudent(String student) {
        final String sql = "SELECT * FROM school JOIN student ON student.school_id = school.id AND lower(student.email) = lower(?); ";
        return jdbc.queryForObject(sql, schoolMapper, student);
    }

    private final static class SchoolMapper implements RowMapper<School> {
        @Override
        public School mapRow(ResultSet rs, int rowNum) throws SQLException {
            School school = new School();
            school.setName(rs.getString("school_name"));
            school.setDev_id(rs.getString("device_id"));
            school.setId(rs.getInt("id"));
            return school;
        }
    }
}
