package com.tpark.back.service.Impl;

import com.tpark.back.dao.Impl.TaskDAOImpl;
import com.tpark.back.dao.TaskDAO;
import com.tpark.back.model.Task;
import com.tpark.back.model.TaskUnit;
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
    public void deleteTask(String admin, int id) {
        taskDAO.deleteTask(admin ,id);
    }

    @Override
    public void changeTask(String admin ,Task task) {
        taskDAO.changeTask(admin ,task);
    }

    @Override
    public void createTask(String admin ,Task task) {
        taskDAO.createTask(admin ,task);
    }

    @Override
    public Task getTask(String admin ,Integer taskId) {
        return taskDAO.getTask(admin ,taskId);
    }

    @Override
    public List<Task> getTasksByUnit(String admin ,Integer unitId) {
        return taskDAO.getTasksByUnit(admin, unitId);
    }

    @Override
    public List<Task> getTasks(String admin) {
        return taskDAO.getAllTasks(admin);
    }

    @Override
    public Object getTaskStudent(Integer taskId, String student) {
        return taskDAO.getTaskStudent(taskId, student);
    }

    @Override
    public Object getTasksByUnitStudent(Integer unitId, String student) {
        return taskDAO.getTasksByUnitStudent(unitId, student);
    }

    @Override
    public void addTaskToUnit(String user, TaskUnit task) {
        taskDAO.addTaskToUnit( user, task);
    }
}
