package com.tpark.back.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class SchoolIDDAO {

    private final JdbcTemplate jdbc;
    private final SchoolMapper schoolMapper = new SchoolMapper();

    @Autowired
    public SchoolIDDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    Integer GetSchoolId(String admin) {
        String sql = "SELECT school_id FROM admin WHERE lower(email) = lower(?);";
        return jdbc.queryForObject(sql, schoolMapper, admin);
    }

    private final class SchoolMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getInt("school_id");
        }


    }

}
