package com.tpark.back.controller;


import com.tpark.back.model.Group;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.GroupService;
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
@RequestMapping("/group")
@EnableJdbcHttpSession
public class GroupController {

    private final GroupService groupService;
    private final AdminService adminService;
    private final StudentService studentService;

    @Autowired
    public GroupController(GroupService groupService, AdminService adminService, StudentService studentService) {
        this.groupService = groupService;
        this.adminService = adminService;
        this.studentService = studentService;
    }


    @GetMapping(path = "/")
    public ResponseEntity getAllGroups(HttpSession session) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        if(session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(groupService.getGroupsForAdmin(session.getAttribute("user").toString()));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(groupService.getGroupsForStudent(session.getAttribute("student").toString()));
        }
    }


    @GetMapping(path = "/{courseId}")
    public ResponseEntity getGroups(HttpSession session,@PathVariable Integer courseId) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        if(session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(groupService.getGroupsByCourse(courseId));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(groupService.getGroupByCourseForStudent(courseId,session.getAttribute("student").toString()));
        }
    }

    @GetMapping(path = "/find/{groupId}")
    public ResponseEntity getGroup(HttpSession session,@PathVariable Integer groupId) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }

        try {
            if(session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(groupService.getGroup(groupId));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(groupService.getGroupForStudent(session.getAttribute("student").toString(),groupId));
            }
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(HttpSession session, @RequestBody Group group) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            groupService.createGroup(group);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserStatus.SUCCESSFULLY_CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(HttpSession session, @RequestBody Group group) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            groupService.changeGroup(group);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_CHANGED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(HttpSession session, @RequestBody int id) {
        if (session.getAttribute("user") == null && session.getAttribute("student") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            groupService.deleteGroup(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_CHANGED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }
}
