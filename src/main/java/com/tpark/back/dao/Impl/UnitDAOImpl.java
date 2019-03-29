package com.tpark.back.dao.Impl;

import com.tpark.back.dao.UnitDAO;
import com.tpark.back.model.dto.UnitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UnitDAOImpl implements UnitDAO {

    private final JdbcTemplate jdbc;

    private final static UnitMapper unitMapper = new UnitMapper();
    private final static IntMapper intMapper = new IntMapper();
    private final SchoolIDDAO schoolIDDAO;

    @Autowired
    public UnitDAOImpl(JdbcTemplate jdbc, SchoolIDDAO schoolIDDAO) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
    }

    @Override
    public List<UnitDTO> getUnitsByCourse(Integer courseId, String email) {
        Integer schoolId = schoolIDDAO.GetSchoolId(email);
        final String sql = "SELECT * FROM unit WHERE course_id = ? AND school_id = ?;";
        return jdbc.query(sql, unitMapper, courseId, schoolId);
    }

    @Override
    public UnitDTO getUnit(Integer unitId, String email) {
        Integer schoolId = schoolIDDAO.GetSchoolId(email);
        final String sql = "SELECT * FROM unit WHERE id = ? AND school_id=?;";
        return jdbc.queryForObject(sql, unitMapper, unitId, schoolId);

    }

    @Override
    public void createUnit(UnitDTO unitDTO, String email) {
        Integer schoolId = schoolIDDAO.GetSchoolId(email);
        final String sql = "INSERT INTO unit(unit_name, course_id, current_position,school_id) VALUES (?, ?, ?,?);";
        jdbc.update(sql, unitDTO.getUnit_name(), unitDTO.getCourse_id(), unitDTO.getPosition(),schoolId);

    }

    @Override
    public void changeUnit(UnitDTO unitDTO, String email) {
        Integer schoolId = schoolIDDAO.GetSchoolId(email);
        final String sql = "UPDATE unit SET unit_name = ?, course_id = ?, current_position=? WHERE id = ? AND school_id=?;";
        jdbc.update(sql, unitDTO.getUnit_name(), unitDTO.getCourse_id(), unitDTO.getPosition(), unitDTO.getId(), schoolId);

    }

    @Override
    public void deleteUnit(int id, String email) {
        Integer schoolId = schoolIDDAO.GetSchoolId(email);
        Integer temp = 0;
        String sql ="SELECT course_id FROM unit WHERE id = ? AND school_id = ?;";
        temp = jdbc.queryForObject(sql,intMapper , id, schoolId);
        sql = "UPDATE group_course SET current_unit = NULL WHERE course_id=? AND school_id = ?";
        jdbc.update(sql,temp, schoolId);
        sql ="DELETE FROM unit WHERE id = ? AND school_id = ?;";
        jdbc.update(sql,id, schoolId);

    }

    @Override
    public UnitDTO getUnitForStudent(Integer unitId, String student) {
        final String sql = "SELECT * FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND student.email = ?)" +
                " AS rg JOIN group_course ON group_course.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id AND unit.id = ?;";
        return jdbc.queryForObject(sql, unitMapper, student, unitId);
    }

    @Override
    public List<UnitDTO> getUnitByCourseForStudent(Integer courseId, String student) {
        final String sql = "SELECT * FROM unit JOIN" +
                "((SELECT email, group_id FROM student JOIN student_group sg " +
                "on student.id = sg.student_id AND student.email = ?)" +
                " AS rg JOIN group_course ON group_course.id = rg.group_id) " +
                "AS g ON g.course_id = unit.course_id AND unit.course_id = ?;";
        return jdbc.query(sql, unitMapper, student, courseId);
    }

    private static final class IntMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            Integer temp = 0;
            temp = resultSet.getInt("course_id");
            return temp;
        }
    }

    private static final class UnitMapper implements RowMapper<UnitDTO> {
        @Override
        public UnitDTO mapRow(ResultSet resultSet, int i) throws SQLException {
            UnitDTO unitDTO = new UnitDTO();
            unitDTO.setId(resultSet.getInt("id"));
            unitDTO.setCourse_id(resultSet.getInt("course_id"));
            unitDTO.setUnit_name(resultSet.getString("unit_name"));
            unitDTO.setPosition(resultSet.getInt("current_position"));
            unitDTO.setDescription(resultSet.getString("description"));
            return unitDTO;
        }
    }
}
