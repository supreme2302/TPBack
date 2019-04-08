package com.tpark.back.mapper;

import com.tpark.back.model.dto.CourseDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseMapper implements RowMapper<CourseDTO> {
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
