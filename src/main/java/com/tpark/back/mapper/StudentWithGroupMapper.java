package com.tpark.back.mapper;

import com.tpark.back.model.dto.StudentWithGroupsDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentWithGroupMapper implements RowMapper<StudentWithGroupsDTO> {
    @Override
    public StudentWithGroupsDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        StudentWithGroupsDTO studentDTO = new StudentWithGroupsDTO();
        studentDTO.setId(resultSet.getInt("id"));
        studentDTO.setEmail(resultSet.getString("email"));
        studentDTO.setName(resultSet.getString("first_name"));
        studentDTO.setSurname(resultSet.getString("last_name"));
        studentDTO.setPassword(resultSet.getString("password"));
        studentDTO.setSchool_id(resultSet.getInt("school_id"));
        return studentDTO;
    }
}