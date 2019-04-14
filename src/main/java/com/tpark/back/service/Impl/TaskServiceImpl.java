package com.tpark.back.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpark.back.dao.Impl.SchoolIDDAO;
import com.tpark.back.dao.Impl.TaskDAOImpl;
import com.tpark.back.model.dto.TaskDTO;
import com.tpark.back.model.dto.TaskUnitDTO;
import com.tpark.back.service.TaskService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final ObjectMapper objectMapper;
    private final TaskDAOImpl taskDAO;
    private final PasswordEncoder passwordEncoder;
    private final SchoolIDDAO schoolIDDAO;

    @Autowired
    public TaskServiceImpl(TaskDAOImpl taskDAO,
                           PasswordEncoder passwordEncoder,
                           ObjectMapper objectMapper,
                           SchoolIDDAO schoolIDDAO) {
        this.taskDAO = taskDAO;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.schoolIDDAO = schoolIDDAO;
    }

    @Override
    public void deleteTask(String admin, int id) {
        taskDAO.deleteTask(admin ,id);
    }

    @Override
    public void changeTask(String admin , TaskDTO taskDTO) {
        taskDAO.changeTask(admin , taskDTO);
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public void createTask(String admin , TaskDTO taskDTO) {
        String task = objectMapper.writeValueAsString(taskDTO.getDataT1());
        taskDTO.setDataT1(task);
        task = objectMapper.writeValueAsString(taskDTO.getDataT2());
        taskDTO.setDataT2(task);
        task = objectMapper.writeValueAsString(taskDTO.getDataT3());
        taskDTO.setDataT3(task);
        taskDAO.createTask(admin , taskDTO);
    }

    @Override
    public TaskDTO getTask(String admin , Integer taskId) {
        return taskDAO.getTask(admin ,taskId);
    }

    @Override
    public List<TaskDTO> getTasksByUnit(String admin , Integer unitId) {
        return taskDAO.getTasksByUnit(admin, unitId);
    }

    @Override
    public List<TaskDTO> getTasks(String admin) {
        Integer schoolId = schoolIDDAO.getSchoolId(admin);
        return taskDAO.getAllTasksBySchoolId(schoolId);
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
    public void addTaskToUnit(String user, TaskUnitDTO task) {
        taskDAO.addTaskToUnit( user, task);
    }
}
