package com.tpark.back.controller;


import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.SchoolService;
import com.tpark.back.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/school")
@EnableJdbcHttpSession
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
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }

        try {
            if (session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(schoolService.getSchoolByAdmin(session.getAttribute("user").toString()));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(schoolService.getSchoolByStudent(session.getAttribute("student").toString()));
            }
                    } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/change")
    public ResponseEntity create(HttpSession session, @RequestBody SchoolDTO schoolDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            schoolService.changeSchool(schoolDTO, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserStatus.SUCCESSFULLY_CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

   }
