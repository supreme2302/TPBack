package com.tpark.back.controller;



import com.tpark.back.model.Course;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.CourseService;
import com.tpark.back.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/course")
//@EnableJdbcHttpSession
public class CourseController {

    private final CourseService courseService;
    private final AdminService adminService;
    private final StudentService studentService;

    @Autowired
    public CourseController(CourseService courseService, AdminService adminService, StudentService studentService) {
        this.courseService = courseService;
        this.adminService = adminService;
        this.studentService = studentService;
    }

    @GetMapping(path = "/{courseId}")
    public ResponseEntity getCourse(HttpSession session,@PathVariable int courseID) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        try {
            if (session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(courseService.getCourse(courseID,session.getAttribute("user").toString()));
            }   else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(courseService.getStudentCourse(courseID,session.getAttribute("student").toString()));
            }
            //TODO: Запилить хранение параметров приложения в базке, и вместе с инфой по приложению кидать сюда json
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/")
    public ResponseEntity getSchoolCourses(HttpSession session) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        try {
            if(session.getAttribute("user") != null){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(courseService.getCoursesByAdmin(session.getAttribute("user").toString()));
            }  else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(courseService.getCoursesByStudent(session.getAttribute("student").toString()));
            }
                   } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(HttpSession session, @RequestBody Course course) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            courseService.createCourse(course, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserStatus.SUCCESSFULLY_CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(HttpSession session, @RequestBody Course course) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            courseService.changeCourse(course, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_CHANGED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/delete")
    public ResponseEntity create(HttpSession session, @RequestBody Integer id) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            courseService.deleteCourse(id, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_CHANGED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

}
