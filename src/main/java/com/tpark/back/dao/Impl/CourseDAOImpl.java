package com.tpark.back.dao.Impl;

import com.tpark.back.dao.CourseDAO;
import com.tpark.back.model.dto.CourseDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAO {

    private final JdbcTemplate jdbc;
    private final static CourseMapper courseMapper = new CourseMapper();
    private final SchoolIDDAO schoolIDDAO;

    @Autowired
    public CourseDAOImpl(JdbcTemplate jdbc, SchoolIDDAO schoolIDDAO) {
        this.jdbc = jdbc;
        this.schoolIDDAO = schoolIDDAO;
    }

    @Override
    public List<CourseDTO> getCoursesByAdmin(String email) {

        final String sql = "SELECT * FROM course JOIN admin ON admin.school_id = course.school_id AND lower(admin.email) = lower(?);";
        return jdbc.query(sql, courseMapper, email);
    }

    @Override
    public void createCourse(CourseDTO courseDTO, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        final String sql = "INSERT INTO course(course_name, school_id) VALUES (?, ?);";
        jdbc.update(sql, courseDTO.getName(),school_id);
    }

    @Override
    public void deleteCourse(int id, String email) {
        Integer school_id = schoolIDDAO.getSchoolId(email);
        String sql = "DELETE FROM group_course WHERE school_id = ? AND course_id = ?;";
        jdbc.update(sql,school_id, id);
        sql = "DELETE FROM course WHERE id = ? AND school_id = ?;\n";
        jdbc.update(sql,id, school_id);
    }

    @Override
    public CourseDTO getCourse(int id, String admin){
        final String sql = "SELECT * FROM course JOIN admin ON course.id=? AND " +
                "lower(admin.email) = lower(?) AND admin.school_id = course.school_id LIMIT 1;";
        return jdbc.queryForObject(sql, courseMapper, id, admin);
    }

    @Override
    public void changeCourse(CourseDTO courseDTO, String admin){
        Integer school_id = schoolIDDAO.getSchoolId(admin);
        final String sql = "UPDATE course SET course_name = ? WHERE id = ? AND school_id = ?;";
        jdbc.update(sql, courseDTO.getName(), courseDTO.getId(),school_id);
    }

    @Override
    public CourseDTO getStudentCourse(Integer courseID, String student) {
        final String sql = "SELECT * FROM course JOIN ((student JOIN student_group ON " +
                "student.id = student_group.student_id AND lower(student.email) = lower(?))" +
                " AS connecti JOIN group_course ON group_course.id = connecti.group_id) AS res" +
                " ON course.id = res.course_id AND course.id = ? LIMIT 1;";
        return jdbc.queryForObject(sql, courseMapper, student, courseID);
    }

    @Override
    public List<CourseDTO> getCoursesByStudent(String student) {
        final String sql = "SELECT * FROM course JOIN ((SELECT group_id FROM student JOIN student_group ON\n" +
                "                student.id = student_group.student_id AND lower(student.email) = lower(?))\n" +
                "                AS connecti JOIN group_course ON group_course.id = connecti.group_id) AS res\n" +
                "                 ON course.id = res.course_id;\n";
        return jdbc.query(sql, courseMapper, student);

    }

    ;

    public static class CourseMapper implements RowMapper<CourseDTO> {
        @Override
        public CourseDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setName(resultSet.getString("course_name"));
            courseDTO.setSchoolId(resultSet.getInt("school_id"));
            courseDTO.setId(resultSet.getInt("id"));
            courseDTO.setDescription(resultSet.getString("description"));
            return courseDTO;
        }
    }
}





