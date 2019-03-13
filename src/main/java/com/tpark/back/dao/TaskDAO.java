package com.tpark.back.dao;

import com.tpark.back.model.Task;

import java.util.List;

public interface TaskDAO {
    void deleteTask(int id);

    void changeTask(Task task);

    void createTask(Task task);

    Task getTask(Integer taskId);

    List<Task> getTasksByUnit(Integer unitId);

    List<Task> getAllTasks(String admin);
}
