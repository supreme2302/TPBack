package com.tpark.back.controller;


import com.tpark.back.model.ChangePassword;
import com.tpark.back.model.School;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.Impl.AdminServiceImpl;
import com.tpark.back.service.Impl.SchoolServiceImpl;
import com.tpark.back.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/school")
public class SchoolController {

    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping(path = "/")
    public ResponseEntity getSchool(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK)
             .body(schoolService.getSchoolByAdmin(session.getAttribute("user").toString()));
            //TODO: Запилить хранение параметров приложения в базке, и вместе с инфой по приложению кидать сюда json

    }

    @PostMapping(path = "/create")
    public ResponseEntity create(HttpSession session, @RequestBody School school) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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
