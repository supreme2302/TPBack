package com.tpark.back.service;

import com.tpark.back.dao.UserDAO;
import com.tpark.back.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUser(String email) {
        return userDAO.getUser(email);
    }

    public void createUser(User user) {
        userDAO.createUser(user);
    }
}
