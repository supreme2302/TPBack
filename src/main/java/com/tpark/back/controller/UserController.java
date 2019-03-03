package com.tpark.back.controller;

import com.tpark.back.model.User;
import com.tpark.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.rmi.AccessException;

@RestController
@RequestMapping("/users")
public class UserController {

    private enum UserStatus {
        ACCESS_ERROR,
        NOT_FOUND,
        ALREADY_AUTHENTICATED,
        SUCCESSFULLY_CREATED,
        NOT_UNIQUE_EMAIL,
        EMPTY_FIELDS_IN_REQUEST,
        WRONG_CREDENTIALS,
        SUCCESSFULLY_AUTHED
    }

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/info")
    public ResponseEntity getUser(HttpSession session) {

        Object sessionAttribute = session.getAttribute("user");
        if (sessionAttribute == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }

        String email = sessionAttribute.toString();

        User user = userService.getUser(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(HttpSession session, @RequestBody User user) {
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ALREADY_AUTHENTICATED);
        }

        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.EMPTY_FIELDS_IN_REQUEST);
        }

        try {
            userService.createUser(user);
            sessionAuth(session, user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserStatus.SUCCESSFULLY_CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.NOT_UNIQUE_EMAIL);
        }
    }

    @PostMapping(path = "/auth")
    public ResponseEntity auth(HttpSession httpSession, @RequestBody User user) {

        if (httpSession.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ALREADY_AUTHENTICATED);
        }

        User userFromDb = userService.getUser(user.getEmail());
        if (userFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }

        boolean userIsValid = userService.checkUserPassword(
                user.getPassword(),
                userFromDb.getPassword());

        if (!userIsValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(UserStatus.WRONG_CREDENTIALS);
        }

        sessionAuth(httpSession, user.getEmail());
        return ResponseEntity.ok(UserStatus.SUCCESSFULLY_AUTHED);
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(HttpSession httpSession, @RequestBody String newPassword) {

        if (httpSession.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }

        try {
            userService.changeUserPassword(httpSession.getAttribute("user").toString(), newPassword);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.WRONG_CREDENTIALS);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }



    private void sessionAuth(HttpSession session, String email){
        session.setAttribute("user",email);
        session.setMaxInactiveInterval(7*24*60*60);
    }

}
