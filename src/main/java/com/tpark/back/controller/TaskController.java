package com.tpark.back.controller;


import com.tpark.back.model.dto.IdDTO;
import com.tpark.back.model.dto.TaskDTO;
import com.tpark.back.model.dto.TaskUnitDTO;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.StudentService;
import com.tpark.back.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

@SuppressWarnings("Duplicates")
@RestController
@RequestMapping("/task")
@EnableJdbcHttpSession
public class TaskController {

    private final TaskService taskService;
    private final AdminService adminService;
    private final StudentService studentService;

    @Autowired
    public TaskController(TaskService taskService, AdminService adminService, StudentService studentService) {
        this.taskService = taskService;
        this.adminService = adminService;
        this.studentService = studentService;
    }

    @GetMapping(path = "/")
    public ResponseEntity getAll(@ApiIgnore HttpSession session ) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTasks(session.getAttribute("user").toString()));
    }

    @GetMapping(path = "/{unitId}")
    public ResponseEntity getTasks(@ApiIgnore HttpSession session, @PathVariable Integer unitId) {

        Object adminSession = session.getAttribute("user");
        Object studentSession = session.getAttribute("student");
        if (session.getAttribute("user") == null && session.getAttribute("student") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }


        if (adminSession != null) {
            if (adminService.getAdminByEmail(adminSession.toString()) == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(UserStatus.ACCESS_ERROR);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskService.getTasksByUnit(adminSession.toString(), unitId));
        }

        else {
            if (studentService.getStudentByEmailWithoutGroupId(studentSession.toString()) == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(UserStatus.ACCESS_ERROR);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskService.getTasksByUnitStudent(unitId, studentSession.toString()));
        }
    }

    @GetMapping(path = "/find/{taskId}")
    public ResponseEntity getTask(@ApiIgnore HttpSession session,@PathVariable Integer taskId) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }

        try {
            if(session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(taskService.getTask(session.getAttribute("user").toString(), taskId));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(taskService.getTaskStudent(taskId, session.getAttribute("student").toString()));

            }
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(@ApiIgnore HttpSession session, @RequestBody TaskDTO taskDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            taskService.createTask(session.getAttribute("user").toString(), taskDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(taskDTO);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity add(@ApiIgnore HttpSession session, @RequestBody TaskUnitDTO task) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            taskService.addTaskToUnit(session.getAttribute("user").toString(), task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(task);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(@ApiIgnore HttpSession session, @RequestBody TaskDTO taskDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            taskService.changeTask(session.getAttribute("user").toString(), taskDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskDTO);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(@ApiIgnore HttpSession session, @RequestBody IdDTO idDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            taskService.deleteTask(session.getAttribute("user").toString(), idDTO.getId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_DELETED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }
}
