package com.tpark.back.controller;


import com.tpark.back.model.School;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.SchoolService;
import com.tpark.back.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/school")
@CrossOrigin(origins = {"http://localhost:8080"}, allowCredentials = "true")
public class SchoolController {

    private final SchoolService schoolService;
    private final AdminService adminService;
    private final StudentService studentService;

    @Autowired
    public SchoolController(SchoolService schoolService, AdminService adminService, StudentService studentService) {
        this.schoolService = schoolService;
        this.adminService = adminService;
        this.studentService = studentService;
    }

    @GetMapping(path = "/")
    public ResponseEntity getSchool(HttpSession session) {
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
                    .body(schoolService.getSchoolByAdmin(session.getAttribute("user").toString()));
                    } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(HttpSession session, @RequestBody School school) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            schoolService.createSchool(school);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserStatus.SUCCESSFULLY_CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

}
