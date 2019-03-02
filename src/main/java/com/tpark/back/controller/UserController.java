package com.tpark.back.controller;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping(path = "/{user}")
    public ResponseEntity getUser(@PathVariable("user") String email) {
        User user = userService.getUser(email);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(HttpSession session, @RequestBody User user) {
        if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        try{
            userService.createUser(user);
            sessionAuth(session, user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch(DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping(path = "/auth")
    public ResponseEntity register(@RequestBody User user) {
        try{
            userService.auth(user);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch(DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }


    public void sessionAuth(HttpSession session, String email){
        session.setAttribute("user",email);
        session.setMaxInactiveInterval(7*24*60*60);
    }

}
