package com.tpark.back.dao;

import com.tpark.back.model.Task;
import com.tpark.back.model.TaskUnit;

import java.util.List;

public interface TaskDAO {
    void deleteTask(String admin, int id);

    void changeTask(String admin ,Task task);

    void createTask(String admin ,Task task);

    Task getTask(String admin ,Integer taskId);

    List<Task> getTasksByUnit(String admin ,Integer unitId);

    List<Task> getAllTasks(String admin);

    Object getTaskStudent(Integer taskId, String student);

    Object getTasksByUnitStudent(Integer unitId, String student) ;

    void addTaskToUnit(String user, TaskUnit task);
}
