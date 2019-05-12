package com.tpark.back.controller;

import com.tpark.back.model.dto.ChangePasswordDTO;
import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.UserStatus;
import com.tpark.back.model.dto.IdDTO;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
@EnableJdbcHttpSession
public class AdminController {

    private final AdminService adminService;
    private final SchoolService schoolService;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(AdminService adminService, SchoolService schoolService) {
        this.adminService = adminService;
        this.schoolService = schoolService;
    }

    @GetMapping(path = "/info")
    public ResponseEntity getUser(@ApiIgnore HttpSession session) {

        logger.info("info session - ", session.getId());

        Object sessionAttribute = session.getAttribute("user");
        if (sessionAttribute == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }

        String email = sessionAttribute.toString();

        AdminDTO user = adminService.getAdminByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity getAllAdmins(@ApiIgnore HttpSession session) {

        logger.info("info session - ", session.getId());

        Object sessionAttribute = session.getAttribute("user");
        if (sessionAttribute == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }

        String email = sessionAttribute.toString();

        AdminDTO user = adminService.getAdminByEmail(email);

        if (user == null ||
                schoolService.getSchoolByAdmin(sessionAttribute.toString()).getAdmin()!=user.getId()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(adminService.getSchoolAdmins(user));
        }
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(@ApiIgnore HttpSession session, @RequestBody AdminDTO user) {

        logger.info("register session - ", session.getId());

        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ALREADY_AUTHENTICATED);
        }

        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.EMPTY_FIELDS_IN_REQUEST);
        }

        try {
            adminService.addAdminAndCreateSchool(user);
            sessionAuth(session, user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserStatus.SUCCESSFULLY_CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.NOT_UNIQUE_FIELDS_IN_REQUEST);
        }
    }

    @PostMapping(path = "/auth")
    public ResponseEntity auth(@ApiIgnore HttpSession httpSession, @RequestBody AdminDTO adminDTO) {

        if (httpSession.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ALREADY_AUTHENTICATED);
        }

        AdminDTO userFromDb = adminService.getAdminByEmail(adminDTO.getEmail());
        if (userFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }

        boolean userIsValid = adminService.checkAdminPassword(
                adminDTO.getPassword(),
                userFromDb.getPassword());

        if (!userIsValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(UserStatus.WRONG_CREDENTIALS);
        }

        sessionAuth(httpSession, adminDTO.getEmail());
        return ResponseEntity.ok(UserStatus.SUCCESSFULLY_AUTHED);
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(@ApiIgnore HttpSession httpSession,
                                 @RequestBody ChangePasswordDTO changePassword) {

        Object userSession = httpSession.getAttribute("user");
        if (userSession == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }

        AdminDTO userFromDb = adminService.getAdminByEmail(userSession.toString());
        boolean passwordIsValid = adminService.checkAdminPassword(
                changePassword.getOldPassword(),
                userFromDb.getPassword());

        if (passwordIsValid) {
            adminService.changeAdminPassword(userSession.toString(),
                    changePassword.getNewPassword());
            return ResponseEntity.ok(UserStatus.SUCCESSFULLY_CHANGED);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserStatus.WRONG_CREDENTIALS);
    }


    @PostMapping(path = "/delete")
    public ResponseEntity delete(@ApiIgnore HttpSession httpSession,
                                 @RequestBody IdDTO idDTO) {

        Object userSession = httpSession.getAttribute("user");
        if (userSession == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }

        AdminDTO userFromDb = adminService.getAdminByEmail(userSession.toString());
        if (schoolService.getSchoolByAdmin(userFromDb.getEmail()).getAdmin() == userFromDb.getId()) {
            try {
                adminService.deleteAdmin(idDTO, userFromDb);
                return ResponseEntity.ok(UserStatus.SUCCESSFULLY_CHANGED);
            } catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(UserStatus.WRONG_CREDENTIALS);
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
    }


    @PostMapping(path = "/add")
    public ResponseEntity add(@ApiIgnore HttpSession httpSession,
                                 @RequestBody AdminDTO adminDTO) {

        Object userSession = httpSession.getAttribute("user");
        if (userSession == null ||
                adminService.getAdminByEmail(httpSession.getAttribute("user").toString()).getId()!=
                        schoolService.getSchoolByAdmin(httpSession.getAttribute("user").toString()).getAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }

        try {
            adminService.addNewAdmin(userSession.toString(),
                    adminDTO);
            return ResponseEntity.ok(UserStatus.SUCCESSFULLY_CHANGED);
        } catch (DuplicateKeyException exp){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserStatus.NOT_UNIQUE_FIELDS_IN_REQUEST);
        }

    }

    @PostMapping(path = "/logout")
    public ResponseEntity logout(@ApiIgnore HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        httpSession.invalidate();
        return ResponseEntity.ok(UserStatus.SUCCESSFULLY_LOGGED_OUT);
    }


    private void sessionAuth(HttpSession session, String email){
        session.setAttribute("user", email);
        session.setMaxInactiveInterval(7*24*60*60);
    }
}
