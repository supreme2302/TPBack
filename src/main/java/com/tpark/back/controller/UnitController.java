package com.tpark.back.controller;

import com.tpark.back.model.dto.IdDTO;
import com.tpark.back.model.dto.UnitDTO;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.Impl.UnitServiceImpl;
import com.tpark.back.service.StudentService;
import com.tpark.back.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/unit")
@EnableJdbcHttpSession
public class UnitController {

    private final UnitService unitService;
    private final AdminService adminService;
    private final StudentService studentService;

    @Autowired
    public UnitController(UnitService unitService, AdminService adminService, StudentService studentService) {
        this.unitService = unitService;
        this.adminService = adminService;
        this.studentService = studentService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity getUnitsByCourse(@ApiIgnore HttpSession session) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(unitService.getAllUnits(session.getAttribute("user").toString()));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(unitService.getAllUnitsForStudent(session.getAttribute("student").toString()));
        }

    }

    @GetMapping(path = "/{courseId}")
    public ResponseEntity getUnitsByCourse(@ApiIgnore HttpSession session, @PathVariable Integer courseId) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(unitService.getUnitsByCourse(courseId, session.getAttribute("user").toString()));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(unitService.getUnitsByCourseForStudent(courseId, session.getAttribute("student").toString()));
        }

    }

    @GetMapping(path = "/find/{unitId}")
    public ResponseEntity getUnitById(@ApiIgnore HttpSession session, @PathVariable Integer unitId) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }

        try {
            if (session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(unitService.getUnit(unitId, session.getAttribute("user").toString()));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(unitService.getUnitForStudent(unitId, session.getAttribute("student").toString()));

            }
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(@ApiIgnore HttpSession session, @RequestBody UnitDTO unitDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            unitService.createUnit(unitDTO, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(unitDTO);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(@ApiIgnore HttpSession session, @RequestBody UnitDTO unitDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            unitService.changeUnit(unitDTO, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(unitDTO);
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
            unitService.deleteUnit(idDTO.getId(), session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_DELETED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }
}
