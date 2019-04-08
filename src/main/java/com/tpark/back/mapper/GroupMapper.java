package com.tpark.back.mapper;

import com.tpark.back.model.dto.GroupDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupMapper implements RowMapper<GroupDTO> {
    @Override
    public GroupDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(resultSet.getInt("id"));
        groupDTO.setName(resultSet.getString("group_name"));
        groupDTO.setCourse_id(resultSet.getInt("course_id"));
        groupDTO.setCurr_unit(resultSet.getInt("current_unit"));
        groupDTO.setDescription(resultSet.getString("description"));
        return groupDTO;
    }
}
