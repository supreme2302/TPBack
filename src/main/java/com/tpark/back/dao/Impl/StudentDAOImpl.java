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
import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO {


    private final JdbcTemplate jdbc;

    private final static StudentMapper studentMapper = new StudentMapper();
    private final static GroupMapper groupMapper = new GroupMapper();

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
        final String sql = "SELECT id, email, first_name, last_name, password, school_id " +
                "FROM student WHERE lower(email) = lower(?)";
        try {
            return jdbc.queryForObject(sql, ((resultSet, i) -> {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setEmail(resultSet.getString("email"));
                student.setName(resultSet.getString("first_name"));
                student.setSurname(resultSet.getString("last_name"));
                student.setPassword(resultSet.getString("password"));
                student.setSchool_id(resultSet.getInt("school_id"));
                return student;
            }), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Student> getStudentsFromGroupById(int id) {
        final String sql = "SELECT * FROM student "
                + "JOIN student_group g on student.id = g.student_id "
                + "JOIN group_course gc on g.group_id = gc.id WHERE gc.id = ?";
        return jdbc.query(sql, studentMapper, id);
    }

    @Override
    public Student getStudentByEmailWithGroupId(String email) {

        String sql = "SELECT student.id, email, first_name, last_name, password, school_id " +
                "FROM student WHERE lower(email) = lower(?)";
        try {
            Student student =  jdbc.queryForObject(sql, studentMapper, email);
            sql = "SELECT group_id FROM student_group WHERE student_id = ?;";
            List<Integer> groups = jdbc.query(sql,groupMapper,student.getId());
            student.setGroup_id(groups);
            return student;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static final class GroupMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getInt("group_id");
        }
    }

    private static final class StudentMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet resultSet, int i) throws SQLException {
            Student student = new Student();
            student.setId(resultSet.getInt("id"));
            student.setEmail(resultSet.getString("email"));
            student.setName(resultSet.getString("first_name"));
            student.setSurname(resultSet.getString("last_name"));
            student.setPassword(resultSet.getString("password"));
            student.setSchool_id(resultSet.getInt("school_id"));
            return student;
        }
    }
}
