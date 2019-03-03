package com.tpark.back.service;

import com.tpark.back.dao.UserDAO;
import com.tpark.back.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(String email) {
        return userDAO.getUser(email);
    }

    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.createUser(user);
    }

    public boolean checkUserPassword(String rawPassword, String passwordFromDb) {
        return passwordEncoder.matches(rawPassword, passwordFromDb);
    }

    public void changeUserPassword(String email, String password) {
        userDAO.changePassword(email,password);
    }
}
