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
        final String sql = "DELETE FROM course WHERE id = ?;";
        jdbc.update(sql,id);
    }

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


