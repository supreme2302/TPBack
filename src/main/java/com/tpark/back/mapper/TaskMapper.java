package com.tpark.back.mapper;

import com.tpark.back.model.dto.TaskDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class TaskMapper implements RowMapper<TaskDTO> {
    @Override
    public TaskDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(resultSet.getInt("id"));
        taskDTO.setTask_type(resultSet.getInt("task_type"));
        taskDTO.setName(resultSet.getString("name"));
        taskDTO.setTaskT1(resultSet.getObject("task_val"));
        taskDTO.setTaskT2(resultSet.getObject("task_val2"));
        taskDTO.setTaskT3(resultSet.getObject("task_val3"));
        return taskDTO;
    }
}
