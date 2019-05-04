package com.tpark.back.mapper;

import com.tpark.back.model.dto.AdminDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AdminMapper implements RowMapper<AdminDTO> {
    @Override
    public AdminDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setSchoolId(resultSet.getInt("school_id"));
        adminDTO.setEmail(resultSet.getString("email"));
        adminDTO.setPassword(resultSet.getString("password"));
        adminDTO.setId(resultSet.getInt("id"));
        return adminDTO;
    }
}