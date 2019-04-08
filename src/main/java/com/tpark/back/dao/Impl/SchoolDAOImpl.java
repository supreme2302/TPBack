package com.tpark.back.dao.Impl;

import com.tpark.back.dao.SchoolDAO;
import com.tpark.back.mapper.SchoolMapper;
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
    private final SchoolMapper schoolMapper;
    private final static AdminIDMapper adminIdMapper = new AdminIDMapper();
    private final Logger logger = LoggerFactory.getLogger(SchoolDAOImpl.class);

    @Autowired
    public SchoolDAOImpl(JdbcTemplate jdbc,
                         SchoolMapper schoolMapper) {
        this.jdbc = jdbc;
        this.schoolMapper = schoolMapper;
    }

    @Transactional
    @Override
    public void createSchool(String schoolName, int id) {
        logger.info("createSchool -  in");
        final String sqlForInsert = "INSERT INTO school(school_name, ownerid,main_color,secondary_color,school_logo,language)  VALUES (?,?,?,?,?,?) RETURNING id";
        Integer schoolId;
        try {
            schoolId = jdbc.queryForObject(sqlForInsert, Integer.class, schoolName, id,"#3F51B5","#303F9F",null,"english");
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

    @Transactional
    @Override
    public void changeSchool(SchoolDTO school, String user) {

        final String sqlForSelect = "SELECT id FROM admin WHERE email = ?;";
        int id = jdbc.queryForObject(sqlForSelect, adminIdMapper,user);
        final String sqlForInsert = "UPDATE school SET main_color=?, school_name=?, secondary_color=?, language=?, school_logo=? WHERE ownerid=?;";
        jdbc.update(sqlForInsert, school.getMain_color(),school.getName(), school.getSecondary_color(),school.getLanguage(),school.getSchool_logo(), id);
        logger.info("createSchool -  created");
    }

    private final static class AdminIDMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("id");
        }
    }
}
