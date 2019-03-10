package com.tpark.back.dao.Impl;

import com.tpark.back.dao.StudentDAO;
import com.tpark.back.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Override
    public Student getStudentByEmailWithoutGroupId(String email) {
        final String sql = "SELECT email, first_name, last_name, password, school_id " +
                "FROM student WHERE lower(email) = lower(?)";
        return jdbc.queryForObject(sql, ((resultSet, i) -> {
            Student student = new Student();
            student.setEmail(resultSet.getString("email"));
            student.setName(resultSet.getString("first_name"));
            student.setSurname(resultSet.getString("last_name"));
            student.setPassword(resultSet.getString("password"));
            student.setSchool_id(resultSet.getInt("school_id"));
            return student;
        }), email);
    }

    @Override
    public Student getStudentByEmailWithGroupId(String email) {
        final String sql = "SELECT email, first_name, last_name, password, group_id, school_id " +
                "FROM student JOIN student_group g on student.id = g.student_id " +
                "WHERE lower(email) = lower(?)";
        try {
            return jdbc.queryForObject(sql, studentMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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
            student.setSchool_id(resultSet.getInt("school_id"));
            return student;
        }
    }
}
