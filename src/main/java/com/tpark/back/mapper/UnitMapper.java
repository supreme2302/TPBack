package com.tpark.back.mapper;

import com.tpark.back.model.dto.UnitDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UnitMapper implements RowMapper<UnitDTO> {
    @Override
    public UnitDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setId(resultSet.getInt("id"));
        unitDTO.setCourse_id(resultSet.getInt("course_id"));
        unitDTO.setUnit_name(resultSet.getString("unit_name"));
        unitDTO.setPrev_pos(resultSet.getInt("prev_unit"));
        unitDTO.setNext_pos(resultSet.getInt("next_unit"));
        unitDTO.setDescription(resultSet.getString("description"));
        return unitDTO;
    }
}
