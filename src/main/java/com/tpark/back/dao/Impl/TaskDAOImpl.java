package com.tpark.back.dao.Impl;

import com.tpark.back.dao.TaskDAO;
import com.tpark.back.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TaskDAOImpl implements TaskDAO {

    private final JdbcTemplate jdbc;

    private final static TaskMapper taskMapper = new TaskMapper();

    @Autowired
    public TaskDAOImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }



    @Override
    public void deleteTask(int id) {
        final String sql ="DELETE FROM task WHERE id = ?;";
        jdbc.update(sql,id);
    }

    @Override
    public void changeTask(Task task) {
        final String sql = "UPDATE task SET description = ?, task_ref = ?, task_type=? WHERE id = ?;";
        jdbc.update(sql,task.getDescription(), task.getTask_ref(), task.getTask_type(), task.getId());

    }

    @Override
    public void createTask(Task task) {
        final String sql = "INSERT INTO task(description, task_ref, task_type) VALUES (?, ?, ?);";
        jdbc.update(sql, task.getDescription(), task.getTask_ref(), task.getTask_type());
    }

    @Override
    public Task getTask(Integer taskId) {
        return null;
    }

    @Override
    public List<Task> getTasksByUnit(Integer unitId) {
        return null;
    }

    @Override
    public List<Task> getAllTasks(String admin) {
        return null;
    }

    private static final class TaskMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet resultSet, int i) throws SQLException {
            Task task = new Task();
            task.setId(resultSet.getInt("id"));
            task.setTask_type(resultSet.getInt("unit_id"));
            task.setTask_ref(resultSet.getString("task_ref"));
            task.setDescription(resultSet.getString("description"));
            return task;
        }
    }
}
