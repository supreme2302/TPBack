package com.tpark.back.mapper;

import com.tpark.back.model.domain.UnitDomain;
import com.tpark.back.model.dto.UnitDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UnitMapper implements RowMapper<UnitDomain> {
    @Override
    public UnitDomain mapRow(ResultSet resultSet, int i) throws SQLException {
        return UnitDomain.builder()
                .id(resultSet.getInt("id"))
                .course_id(resultSet.getInt("course_id"))
                .unit_name(resultSet.getString("unit_name"))
                .prev_pos(resultSet.getInt("prev_unit"))
                .next_pos(resultSet.getInt("next_unit"))
                .description(resultSet.getString("description"))
                .tags(resultSet.getString("tags"))
                .build();
    }
}
