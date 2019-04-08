package com.tpark.back.mapper;

import com.tpark.back.model.dto.SchoolDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SchoolMapper implements RowMapper<SchoolDTO> {
    @Override
    public SchoolDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setName(rs.getString("school_name"));
        schoolDTO.setId(rs.getInt("id"));
        schoolDTO.setAdmin(rs.getInt("ownerid"));
        return schoolDTO;
    }
}
