package com.tpark.back.service.Impl;

import com.tpark.back.dao.Impl.TaskDAOImpl;
import com.tpark.back.dao.TaskDAO;
import com.tpark.back.model.Task;
import com.tpark.back.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDAOImpl taskDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TaskServiceImpl(TaskDAOImpl taskDAO, PasswordEncoder passwordEncoder) {
        this.taskDAO = taskDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void deleteTask(int id) {
        taskDAO.deleteTask(id);
    }

    @Override
    public void changeTask(Task task) {
        taskDAO.changeTask(task);
    }

    @Override
    public void createTask(Task task) {
        taskDAO.createTask(task);
    }

    @Override
    public Task getTask(Integer taskId) {
        return taskDAO.getTask(taskId);
    }

    @Override
    public List<Task> getTasksByUnit(Integer unitId) {
        return taskDAO.getTasksByUnit(unitId);
    }

    @Override
    public List<Task> getTasks(String admin) {
        return taskDAO.getAllTasks(admin);
    }
}
