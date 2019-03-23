package com.tpark.back.dao.Impl;

import com.tpark.back.dao.SchoolIDDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class SchoolIDDAOImpl implements SchoolIDDAO {
    private final DataSource dataSource;
    private JdbcTemplate jdbc;
    private final SchoolMapper schoolMapper = new SchoolMapper();

    @Autowired
    public SchoolIDDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() {
        this.jdbc = new JdbcTemplate(this.dataSource);
    }

    public Integer getSchoolId(String admin) {
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
