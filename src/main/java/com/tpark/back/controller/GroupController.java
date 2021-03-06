package com.tpark.back.controller;


import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.GroupDTO;
import com.tpark.back.model.UserStatus;
import com.tpark.back.model.dto.IdDTO;
import com.tpark.back.model.dto.StudentDTO;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    public ResponseEntity getAllGroups(@ApiIgnore HttpSession session) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        try {


            if (session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(groupService.getGroupsForAdmin(session.getAttribute("user").toString()));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(groupService.getGroupsForStudent(session.getAttribute("student").toString()));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }


    @GetMapping(path = "/{courseId}")
    public ResponseEntity getGroups(@ApiIgnore HttpSession session,@PathVariable Integer courseId) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        try {
            if (session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(groupService.getGroupsByCourse(courseId, session.getAttribute("user").toString()));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(groupService.getGroupByCourseForStudent(courseId, session.getAttribute("student").toString()));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/find/{groupId}")
    public ResponseEntity getGroup(@ApiIgnore HttpSession session,@PathVariable Integer groupId) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        try {
            if(session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(groupService.getGroup(groupId,session.getAttribute("user").toString()));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(groupService.getGroupForStudent(session.getAttribute("student").toString(),groupId));
            }
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

//    @GetMapping(path = "/students/{groupId}")
//    public ResponseEntity getStudentsByGroupId(HttpSession httpSession,
//                                               @PathVariable(name = "groupId") Integer groupId) {
//        Object adminSession = httpSession.getAttribute("user");
//        if (adminSession == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserStatus.ACCESS_ERROR);
//        }
//        String adminEmailFromSession = adminSession.toString();
//        AdminDTO adminDTO = adminService.getAdminByEmail(adminEmailFromSession);
//        if (adminDTO == null) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ACCESS_ERROR);
//        }
//        List<StudentDTO> students = studentService;
//
//    }

    @PostMapping(path = "/create")
    public ResponseEntity create(@ApiIgnore HttpSession session, @RequestBody GroupDTO groupDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            groupService.createGroup(groupDTO, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(groupDTO);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/edit")
    public ResponseEntity changeGroup(@ApiIgnore HttpSession session, @RequestBody GroupDTO groupDTO) {
        System.out.println("editing");
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            groupService.changeGroup(groupDTO, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(groupDTO);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(@ApiIgnore HttpSession session, @RequestBody IdDTO idDTO) {
        if (session.getAttribute("user") == null && session.getAttribute("student") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            groupService.deleteGroup(idDTO.getId() ,session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_DELETED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }
}
