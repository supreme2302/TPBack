package com.tpark.back.dao.Impl;

import com.tpark.back.dao.SchoolDAO;
import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.model.exception.ConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class SchoolDAOImpl implements SchoolDAO {
    private final JdbcTemplate jdbc;
    private final static SchoolMapper schoolMapper = new SchoolMapper();

    private final Logger logger = LoggerFactory.getLogger(SchoolDAOImpl.class);

    @Autowired
    public SchoolDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    @Override
    public void createSchool(String schoolName, int id) {
        logger.info("createSchool -  in");
        final String sqlForInsert = "INSERT INTO school(school_name, ownerid)  VALUES (?,?) RETURNING id";
        Integer schoolId;
        try {
            schoolId = jdbc.queryForObject(sqlForInsert, Integer.class, schoolName, id);
            logger.info("createSchool -  created");
        } catch (DuplicateKeyException e) {
            throw new ConflictException();
        }
        final String sqlForUpdate = "UPDATE admin SET school_id = ? WHERE id = ?";
        jdbc.update(sqlForUpdate, schoolId, id);
    }

    @Override
    public SchoolDTO getSchoolByAdmin(String admin) {
        final String sql = "SELECT * FROM school JOIN admin ON admin.school_id = school.id AND lower(admin.email) = lower(?); ";
        return jdbc.queryForObject(sql, schoolMapper, admin);
    }

    @Override
    public SchoolDTO getSchoolByStudent(String student) {
        final String sql = "SELECT * FROM school JOIN student ON student.school_id = school.id AND lower(student.email) = lower(?); ";
        return jdbc.queryForObject(sql, schoolMapper, student);
    }

    private final static class AdminIDMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("id");
        }
    }

    private final static class SchoolMapper implements RowMapper<SchoolDTO> {
        @Override
        public SchoolDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            SchoolDTO schoolDTO = new SchoolDTO();
            schoolDTO.setName(rs.getString("school_name"));
            schoolDTO.setId(rs.getInt("id"));
            schoolDTO.setAdmin(rs.getInt("ownerid"));
            return schoolDTO;
        }
    }
}
