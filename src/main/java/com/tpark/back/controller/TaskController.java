package com.tpark.back.controller;


import com.tpark.back.model.Task;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.StudentService;
import com.tpark.back.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = {"http://localhost:8080"}, allowCredentials = "true")
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
    public ResponseEntity getAll(HttpSession session ) {
        if (session.getAttribute("user") == null && session.getAttribute("student") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null &&
                studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTasks(session.getAttribute("user").toString()));
        //TODO: Промежуточная таблица для taskов

    }

    @GetMapping(path = "/{unitId}")
    public ResponseEntity getTasks(HttpSession session, @PathVariable Integer unitId) {
        if (session.getAttribute("user") == null && session.getAttribute("student") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null &&
                studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTasksByUnit(unitId));
        //TODO: Промежуточная таблица для taskов

    }

    @GetMapping(path = "/find/{taskId}")
    public ResponseEntity getTask(HttpSession session,@PathVariable Integer taskId) {
        if (session.getAttribute("user") == null && session.getAttribute("student") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null &&
                studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(taskService.getTask(taskId));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(HttpSession session, @RequestBody Task task) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserStatus.SUCCESSFULLY_CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(HttpSession session, @RequestBody Task task) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            taskService.changeTask(task);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_CHANGED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(HttpSession session, @RequestBody int id) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            taskService.deleteTask(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_CHANGED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }
}