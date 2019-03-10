package com.tpark.back.dao.Impl;

import com.tpark.back.dao.StudentDAO;
import com.tpark.back.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class StudentDAOImpl implements StudentDAO {


    private final JdbcTemplate jdbc;

    private final static StudentMapper studentMapper = new StudentMapper();

    @Autowired
    public StudentDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void addStudent(Student student) {
        final String sql = "INSERT INTO student(email, first_name, last_name, password, school_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, student.getEmail(), student.getName(), student.getSurname(),
                student.getPassword(), student.getSchool_id());
    }

    private static final class StudentMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet resultSet, int i) throws SQLException {
            Student student = new Student();
            student.setEmail(resultSet.getString("email"));
            student.setName(resultSet.getString("first_name"));
            student.setSurname(resultSet.getString("last_name"));
            student.setGroup_id(resultSet.getString("group_id"));
            student.setPassword(resultSet.getString("password"));
            return student;
        }
    }
}
