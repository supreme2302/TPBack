package com.tpark.back.dao.Impl;

import com.tpark.back.dao.UnitDAO;
import com.tpark.back.model.Unit;
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

    @Autowired
    public UnitDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    public List<Unit> getUnitsByCourse(Integer courseId) {
        final String sql = "SELECT * FROM unit WHERE course_id = ?;";
        return jdbc.query(sql, unitMapper, courseId);
    }

    public Unit getUnit(Integer unitId) {

        final String sql = "SELECT * FROM unit WHERE id = ?;";
        return jdbc.queryForObject(sql, unitMapper, unitId);

    }

    public void createUnit(Unit unit) {
        final String sql = "INSERT INTO unit(unit_name, course_id, current_position) VALUES (?, ?, ?);";
        jdbc.update(sql,unit.getUnit_name(), unit.getCourse_id(), unit.getPosition());

    }

    public void changeUnit(Unit unit) {
        final String sql = "UPDATE unit SET unit_name = ?, course_id = ?, current_position=? WHERE id = ?;";
        jdbc.update(sql,unit.getUnit_name(), unit.getCourse_id(), unit.getPosition(), unit.getId());

    }

    public void deleteUnit(int id) {
        Integer temp = 0;
        String sql ="SELECT course_id FROM unit WHERE id = ?;";
        temp = jdbc.queryForObject(sql,intMapper , id);
        sql = "UPDATE group_course SET current_unit = NULL WHERE course_id=?";
        jdbc.update(sql,temp);
        sql ="DELETE FROM unit WHERE id = ?;";
        jdbc.update(sql,id);

    }

    private static final class IntMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            Integer temp = 0;
            temp = resultSet.getInt("course_id");
            return temp;
        }
    }

    private static final class UnitMapper implements RowMapper<Unit> {
        @Override
        public Unit mapRow(ResultSet resultSet, int i) throws SQLException {
            Unit unit = new Unit();
            unit.setId(resultSet.getInt("id"));
            unit.setCourse_id(resultSet.getInt("course_id"));
            unit.setUnit_name(resultSet.getString("unit_name"));
            unit.setPosition(resultSet.getInt("current_position"));
            return unit;
        }
    }
}
