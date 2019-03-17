package com.tpark.back.dao.Impl;

import com.tpark.back.dao.CourseDAO;
import com.tpark.back.model.Course;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Autowired
    public CourseDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Course> getCoursesByAdmin(String email) {

        final String sql = "SELECT * FROM course JOIN admin ON admin.school_id = course.school_id AND lower(admin.email) = lower(?);";
        return jdbc.query(sql, courseMapper, email);
    }

    @Override
    public void createCourse(Course course) {
        final String sql = "INSERT INTO course(course_name, school_id) VALUES (?, ?);";
        jdbc.update(sql,course.getName(),course.getSchoolId());
    }

    @Override
    public void deleteCourse(int id) {
        String sql = "DELETE FROM group_course WHERE course_id = ?;";
        jdbc.update(sql,id);
        sql = "DELETE FROM course WHERE id = ?;\n";
        jdbc.update(sql,id);
    }

    @Override
    public Course getCourse(int id, String admin){
        final String sql = "SELECT * FROM course JOIN admin ON course.id=? AND lower(admin.email) = lower(?) LIMIT 1;";
        return jdbc.queryForObject(sql, courseMapper, id, admin);
    }

    @Override
    public void changeCourse(Course course){
        final String sql = "UPDATE course SET course_name = ? WHERE id = ?;";
        jdbc.update(sql,course.getName(), course.getId());
    }

    @Override
    public Course getStudentCourse(Integer courseID, String student) {
        final String sql = "SELECT * FROM course JOIN ((student JOIN student_group ON " +
                "student.id = student_group.student_id AND lower(student.email) = lower(?))" +
                " AS connecti JOIN group_course ON group_course.id = connecti.group_id) AS res" +
                " ON course.id = res.course_id AND course.id = ? LIMIT 1;";
        return jdbc.queryForObject(sql, courseMapper, student, courseID);
    }

    @Override
    public List<Course> getCoursesByStudent(String student) {
        final String sql = "SELECT * FROM course JOIN ((SELECT group_id FROM student JOIN student_group ON\n" +
                "                student.id = student_group.student_id AND lower(student.email) = lower(?))\n" +
                "                AS connecti JOIN group_course ON group_course.id = connecti.group_id) AS res\n" +
                "                 ON course.id = res.course_id;\n";
        return jdbc.query(sql, courseMapper, student);

    }

    ;

    public static class CourseMapper implements RowMapper<Course> {
        @Override
        public Course mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Course course = new Course();
            course.setName(resultSet.getString("course_name"));
            course.setSchoolId(resultSet.getInt("school_id"));
            course.setId(resultSet.getInt("id"));
            return course;
        }
    }
}





