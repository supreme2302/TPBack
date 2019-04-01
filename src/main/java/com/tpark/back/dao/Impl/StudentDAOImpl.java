package com.tpark.back.dao.Impl;

import com.tpark.back.dao.StudentDAO;
import com.tpark.back.model.dto.StudentDTO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO {


    private final JdbcTemplate jdbc;

    private final static StudentMapper studentMapper = new StudentMapper();
    private final static GroupMapper groupMapper = new GroupMapper();
    private final SchoolIDDAO schoolIDDAO;

    @Autowired
    public StudentDAOImpl(JdbcTemplate jdbc, SchoolIDDAO schoolIDDAO) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
    }

    @Override
    @Transactional
    public void addStudent(StudentDTO studentDTO, String admin) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        String sql = "INSERT INTO student(email, first_name, last_name, password, school_id) "
                + "VALUES (?, ?, ?, ?, ?);";
        jdbc.update(sql, studentDTO.getEmail(), studentDTO.getName(), studentDTO.getSurname(),
                studentDTO.getPassword(), school_id);
        sql = "SELECT * FROM student WHERE email=?;";
        StudentDTO res =jdbc.queryForObject(sql,studentMapper,studentDTO.getEmail());
        Integer i = 0;
        sql = "INSERT INTO student_group(group_id, student_id) VALUES (?,?);";
        while ( i < studentDTO.getGroup_id().size()){
            jdbc.update(sql, studentDTO.getGroup_id().get(i), res.getId());
            i++;
        }
    }

    @Override
    public StudentDTO getStudentByEmailWithoutGroupId(String email) {
        final String sql = "SELECT id, email, first_name, last_name, password, school_id " +
                "FROM student WHERE lower(email) = lower(?)";
        try {
            return jdbc.queryForObject(sql, ((resultSet, i) -> {
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setId(resultSet.getInt("id"));
                studentDTO.setEmail(resultSet.getString("email"));
                studentDTO.setName(resultSet.getString("first_name"));
                studentDTO.setSurname(resultSet.getString("last_name"));
                studentDTO.setPassword(resultSet.getString("password"));
                studentDTO.setSchool_id(resultSet.getInt("school_id"));
                return studentDTO;
            }), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<StudentDTO> getStudentsFromGroupById(int id) {
        final String sql = "SELECT * FROM student "
                + "JOIN student_group g on student.id = g.student_id "
                + "JOIN group_course gc on g.group_id = gc.id WHERE gc.id = ?";
        return jdbc.query(sql, studentMapper, id);
    }

    @Override
    @Transactional
    public List<StudentDTO> getAllStudents(String admin) {
        String sql = "SELECT student.id, student.email, first_name, last_name, student.password, student.school_id " +
                "FROM student JOIN admin ON admin.email = ? AND admin.school_id = student.school_id";
        try {
            List<StudentDTO> studentDTOS =  jdbc.query(sql, studentMapper, admin);
            sql = "SELECT group_id FROM student_group WHERE student_id = ?;";
            int i = 0;
            while (i< studentDTOS.size()){
                List<Integer> groups = jdbc.query(sql,groupMapper, studentDTOS.get(i).getId());
                studentDTOS.get(i).setGroup_id(groups);
                i++;
            }
            return studentDTOS;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void deleteStudent(Integer id, String admin) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        String sql = "DELETE FROM student WHERE id = ? AND school_id = ?";
        jdbc.update(sql,  id, school_id);

    }

    @Override
    public void changeStudent(StudentDTO studentDTO, String admin) {
        Integer school_id = schoolIDDAO.GetSchoolId(admin);
        String sql = "UPDATE student SET email=?, first_name=?, last_name=?, password=? WHERE id = ? AND school_id = ?;";
        jdbc.update(sql, studentDTO.getEmail(), studentDTO.getName(), studentDTO.getSurname(),
                studentDTO.getPassword(),studentDTO.getId(), school_id);
        sql = "DELETE FROM student_group WHERE student_id = ?;";
        jdbc.update(sql,studentDTO.getId());
        Integer i = 0;
        sql = "INSERT INTO student_group(group_id, student_id) VALUES (?,?);";
        while ( i < studentDTO.getGroup_id().size()){
            jdbc.update(sql, studentDTO.getGroup_id().get(i), studentDTO.getId());
            i++;
        }
    }

    @Override
    @Transactional
    public StudentDTO getStudentByEmailWithGroupId(String email) {

        String sql = "SELECT student.id, email, first_name, last_name, password, school_id " +
                "FROM student WHERE lower(email) = lower(?)";
        try {
            StudentDTO studentDTO =  jdbc.queryForObject(sql, studentMapper, email);
            sql = "SELECT group_id FROM student_group WHERE student_id = ?;";
            List<Integer> groups = jdbc.query(sql,groupMapper, studentDTO.getId());
            studentDTO.setGroup_id(groups);
            return studentDTO;
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

    private static final class StudentMapper implements RowMapper<StudentDTO> {
        @Override
        public StudentDTO mapRow(ResultSet resultSet, int i) throws SQLException {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setId(resultSet.getInt("id"));
            studentDTO.setEmail(resultSet.getString("email"));
            studentDTO.setName(resultSet.getString("first_name"));
            studentDTO.setSurname(resultSet.getString("last_name"));
            studentDTO.setPassword(resultSet.getString("password"));
            studentDTO.setSchool_id(resultSet.getInt("school_id"));
            return studentDTO;
        }
    }
}
