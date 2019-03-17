package com.tpark.back.controller;

import com.tpark.back.model.Admin;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@EnableJdbcHttpSession
@CrossOrigin(origins = {"http://localhost:8080", "https://supreme-spa.firebaseapp.com"}, allowCredentials = "true")
public class TestCont {
    private final AdminService adminService;
    private final Logger LOGGER = LoggerFactory.getLogger(TestCont.class);


    @Autowired
    public TestCont(AdminService adminService) {
        this.adminService = adminService;
    }

    private enum UserStatus {
        SUCCESSFULLY_REGISTERED,
        SUCCESSFULLY_AUTHED,
        SUCCESSFULLY_LOGGED_OUT,
        SUCCESSFULLY_CHANGED,
        SUCCESSFULLY_CREATED,
        ACCESS_ERROR,
        WRONG_CREDENTIALS,
        NOT_UNIQUE_USERNAME_OR_EMAIL,
        NOT_UNIQUE_PHONE,
        ALREADY_AUTHENTICATED,
        UNEXPECTED_ERROR,
        NOT_FOUND,
        MAGIC
    }

    @PostMapping(path = "/create")
    public ResponseEntity createUser(@RequestBody Admin auth,
                                     HttpSession session) {
        Object sessionAttribute = session.getAttribute("user");
        if (sessionAttribute != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body((UserStatus.ALREADY_AUTHENTICATED));
        }


        String email = sessionAttribute.toString();

        Admin user = adminService.getAdminByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(com.tpark.back.model.UserStatus.NOT_FOUND);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }


    @GetMapping(path = "/info")
    public ResponseEntity userInfo(HttpSession session) {
        Object sessionAttr = session.getAttribute("user");
        String userEmail = (String) sessionAttr;
        if (sessionAttr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((UserStatus.ACCESS_ERROR));
        }
        String email = sessionAttr.toString();

        Admin user = adminService.getAdminByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(com.tpark.back.model.UserStatus.NOT_FOUND);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    private void sessionAuth(HttpSession session, Admin auth) {
        session.setAttribute("user", auth.getEmail());
        session.setMaxInactiveInterval(60 * 60);
    }

}

