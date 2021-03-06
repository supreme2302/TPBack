package com.tpark.back.dao;

import com.tpark.back.model.dto.TaskDTO;
import com.tpark.back.model.dto.TaskUnitDTO;

import java.util.List;

public interface TaskDAO {
    void deleteTask(String admin, int id);

    void changeTask(String admin , TaskDTO taskDTO);

    void createTask(String admin , TaskDTO taskDTO);

    TaskDTO getTask(String admin , Integer taskId);

    List<TaskDTO> getTasksByUnit(String admin , Integer unitId);

    List<TaskDTO> getAllTasksBySchoolId(int schoolId);

    Object getTaskStudent(Integer taskId, String student);

    Object getTasksByUnitStudent(Integer unitId, String student) ;

    void addTaskToUnit(String user, TaskUnitDTO task);
}
