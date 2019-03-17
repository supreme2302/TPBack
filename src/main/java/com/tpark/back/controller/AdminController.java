package com.tpark.back.controller;

import com.tpark.back.model.ChangePassword;
import com.tpark.back.model.Admin;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(path = "/info")
    public ResponseEntity getUser(HttpSession session) {

        logger.info("info");

        Object sessionAttribute = session.getAttribute("user");
        if (sessionAttribute == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }

        String email = sessionAttribute.toString();

        Admin user = adminService.getAdminByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(HttpSession session, @RequestBody Admin user) {

        logger.info("register");

        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ALREADY_AUTHENTICATED);
        }

        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.EMPTY_FIELDS_IN_REQUEST);
        }

        logger.info("register - valid");

        try {
            logger.info("register - try");
            adminService.addAdmin(user);
            logger.info("register - added");
            sessionAuth(session, user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserStatus.SUCCESSFULLY_CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.NOT_UNIQUE_EMAIL);
        }
    }

    @PostMapping(path = "/auth")
    public ResponseEntity auth(HttpSession httpSession, @RequestBody Admin admin) {

        if (httpSession.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ALREADY_AUTHENTICATED);
        }

        Admin userFromDb = adminService.getAdminByEmail(admin.getEmail());
        if (userFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }

        boolean userIsValid = adminService.checkAdminPassword(
                admin.getPassword(),
                userFromDb.getPassword());

        if (!userIsValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(UserStatus.WRONG_CREDENTIALS);
        }

        sessionAuth(httpSession, admin.getEmail());
        return ResponseEntity.ok(UserStatus.SUCCESSFULLY_AUTHED);
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(HttpSession httpSession,
                                 @RequestBody ChangePassword changePassword) {

        Object userSession = httpSession.getAttribute("user");
        if (userSession == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }

        Admin userFromDb = adminService.getAdminByEmail(userSession.toString());
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

    @PostMapping(path = "/logout")
    public ResponseEntity logout(HttpSession httpSession) {
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

    @GetMapping(path = "/test")
    public ResponseEntity testGetMethod() {
        logger.info("TEEEEEEESTTT");
        return ResponseEntity.ok("OOOOOOOOOOOOOOOOOKKKKKK");
    }
}
