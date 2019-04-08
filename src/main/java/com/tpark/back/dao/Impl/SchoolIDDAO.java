package com.tpark.back.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolIDDAO {

    private final JdbcTemplate jdbc;

    @Autowired
    public SchoolIDDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Integer getSchoolId(String admin) {
        String sql = "SELECT school_id FROM admin WHERE lower(email) = lower(?);";
        return jdbc.queryForObject(sql, Integer.class, admin);
    }

}
