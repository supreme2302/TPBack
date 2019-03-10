package com.tpark.back.controller;

import com.tpark.back.model.Admin;
import com.tpark.back.model.Student;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.StudentService;
import com.tpark.back.util.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/student")
public class StudentController {

    private final StudentService studentService;
    private final AdminService adminService;

    @Autowired
    public StudentController(StudentService studentService, AdminService adminService) {
        this.studentService = studentService;
        this.adminService = adminService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity createStudent(HttpSession session, @RequestBody Student student) {
        Object adminSession = session.getAttribute("user");
        if (adminSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserStatus.ACCESS_ERROR);
        }

        Admin existingAdmin = adminService.getAdminByEmail(adminSession.toString());

        if (existingAdmin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ACCESS_ERROR);
        }

        String password = RandomString.getShortTokenString();
        student.setPassword(password);
        studentService.addStudent(student);
        student.setPassword(password);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

}
