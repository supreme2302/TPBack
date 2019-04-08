package com.tpark.back.mapper;

import com.tpark.back.model.dto.StudentDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper implements RowMapper<StudentDTO> {
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
