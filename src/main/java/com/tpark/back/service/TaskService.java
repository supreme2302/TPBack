package com.tpark.back.service;

import com.tpark.back.model.Task;

import java.util.List;

public interface TaskService {
    void deleteTask(String admin ,int id);

    void changeTask(String admin ,Task task);

    void createTask(String admin ,Task task);

    Task getTask(String admin ,Integer taskId);

    List<Task> getTasksByUnit(String admin ,Integer unitId);

    List<Task> getTasks(String admin);

    Object getTaskStudent(Integer taskId, String student);

    Object getTasksByUnitStudent(Integer unitId, String student);
}
