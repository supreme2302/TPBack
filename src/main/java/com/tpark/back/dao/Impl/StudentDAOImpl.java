package com.tpark.back.dao.Impl;

import com.tpark.back.dao.StudentDAO;
import com.tpark.back.mapper.GroupMapper;
import com.tpark.back.mapper.StudentMapper;
import com.tpark.back.mapper.StudentWithGroupMapper;
import com.tpark.back.model.dto.GroupDTO;
import com.tpark.back.model.dto.StudentDTO;
import com.tpark.back.model.dto.StudentWithGroupsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@Repository
@Transactional
public class StudentDAOImpl implements StudentDAO {


    private final JdbcTemplate jdbc;

    private final StudentMapper studentMapper;
    private final StudentWithGroupMapper studentWithGroupMapper;
    private final GroupMapper groupMapper;
    private final SchoolIDDAO schoolIDDAO;

    @Autowired
    public StudentDAOImpl(JdbcTemplate jdbc,
                          SchoolIDDAO schoolIDDAO,
                          GroupMapper groupMapper,
                          StudentMapper studentMapper,
                          StudentWithGroupMapper studentWithGroupMapper) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
        this.groupMapper = groupMapper;
        this.studentMapper = studentMapper;
        this.studentWithGroupMapper = studentWithGroupMapper;
    }

    @Override
    @Transactional
    public void addStudent(StudentDTO studentDTO, String admin) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        studentDTO.setSchool_id(school_id);
        String sql = "INSERT INTO student(email, first_name, last_name, password, school_id) "
                + "VALUES (?, ?, ?, ?, ?);";
        jdbc.update(sql, studentDTO.getEmail(), studentDTO.getName(), studentDTO.getSurname(),
                studentDTO.getPassword(), school_id);
        sql = "SELECT * FROM student WHERE email = ?;";
        StudentDTO res =jdbc.queryForObject(sql,studentMapper,studentDTO.getEmail());
        int i = 0;
        sql = "INSERT INTO student_group(group_id, student_id) VALUES (?,?);";
        if (studentDTO.getGroup_id() != null) {
            while ( i < studentDTO.getGroup_id().size()) {
                jdbc.update(sql, studentDTO.getGroup_id().get(i), res.getId());
                i++;
            }
        }
    }

    @Override
    public StudentDTO getStudentByEmailWithoutGroupId(String email) {
        final String sql = "SELECT id, email, first_name, last_name, password, school_id " +
                "FROM student WHERE lower(email) = lower(?)";
        try {
            return jdbc.queryForObject(sql, studentMapper, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<StudentDTO> getStudentsFromGroupById(int id) {
        String sql = "SELECT * FROM student "
                + "JOIN student_group g on student.id = g.student_id "
                + "JOIN group_course gc on g.group_id = gc.id WHERE gc.id = ?";
        try {
            List<StudentDTO> studentDTOS =  jdbc.query(sql, studentMapper, id);
            sql = "SELECT * FROM student_group WHERE student_id = ?;";
            int i = 0;
            while (i< studentDTOS.size()){
                List<Integer> groups = jdbc.query(sql, ((resultSet, i1) -> resultSet.getInt("group_id")), studentDTOS.get(i).getId());
                studentDTOS.get(i).setGroup_id(groups);
                i++;
            }
            return studentDTOS;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<StudentDTO> getAllStudents(String admin) {
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        String sql = "SELECT student.id, student.email, first_name, last_name, student.password, student.school_id " +
                "FROM student WHERE student.school_id=?";
        try {
            List<StudentDTO> studentDTOS =  jdbc.query(sql, studentMapper, school_id);
            sql = "SELECT * FROM student_group WHERE student_id = ?;";
            int i = 0;
            while (i< studentDTOS.size()){
                List<Integer> groups = jdbc.query(sql, ((resultSet, i1) -> resultSet.getInt("group_id")), studentDTOS.get(i).getId());
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
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        jdbc.update("DELETE FROM student_group WHERE student_id = ?", id);
        String sql = "DELETE FROM student WHERE id = ? AND school_id = ?";
        jdbc.update(sql,  id, school_id);

    }

    @Override
    public void changeStudent(StudentDTO studentDTO, String adminEmail) {
        Integer school_id = schoolIDDAO.getSchoolId(adminEmail);
        String sql = "UPDATE student SET email = ?, first_name = ?, last_name = ? WHERE id = ? AND school_id = ?";
        studentDTO.setSchool_id(school_id);
        jdbc.update(sql, studentDTO.getEmail(), studentDTO.getName(), studentDTO.getSurname(),studentDTO.getId(), school_id);
        sql = "DELETE FROM student_group WHERE student_id = ?;";
        jdbc.update(sql, studentDTO.getId());
        int i = 0;
        sql = "INSERT INTO student_group(group_id, student_id) VALUES (?, ?);";
        while ( i < studentDTO.getGroup_id().size()) {
            jdbc.update(sql, studentDTO.getGroup_id().get(i), studentDTO.getId());
            i++;
        }
    }

    @Override
    public StudentWithGroupsDTO getStudentByEmailWithGroups(String email) {
        String sql = "SELECT student.id, email, first_name, last_name, password, school_id " +
                "FROM student WHERE lower(email) = lower(?)";
        try {
            StudentWithGroupsDTO studentDTO =  jdbc.queryForObject(sql, studentWithGroupMapper, email);
            sql = "SELECT group_course.id, group_name, course_id, school_id, description, current_unit FROM group_course " +
                    "JOIN student_group ON student_id = ? AND student_group.group_id = group_course.id;";
            List<GroupDTO> groups = jdbc.query(sql, groupMapper, studentDTO.getId());
            studentDTO.setGroup(groups);
            return studentDTO;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void changePassword(StudentDTO studentDTO) {
        final String sql = "UPDATE student SET password = ? WHERE id = ? AND school_id = ?";
        jdbc.update(sql, studentDTO.getPassword(), studentDTO.getId(), studentDTO.getSchool_id());
    }

    @Override
    public StudentDTO getStudentByEmailWithGroupId(String email) {

        String sql = "SELECT student.id, email, first_name, last_name, password, school_id " +
                "FROM student WHERE lower(email) = lower(?)";
        try {
            StudentDTO studentDTO =  jdbc.queryForObject(sql, studentMapper, email);
            sql = "SELECT group_id FROM student_group WHERE student_id = ?;";
            List<Integer> groups = jdbc.query(sql, (resultSet, i) -> resultSet.getInt("group_id"),
                    studentDTO.getId());
            studentDTO.setGroup_id(groups);
            return studentDTO;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
