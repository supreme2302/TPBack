package com.tpark.back.service.Impl;

import com.tpark.back.dao.Impl.UserDAOImpl;
import com.tpark.back.model.User;
import com.tpark.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAOImpl userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAOImpl userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }


    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.addUser(user);
    }

    public boolean checkUserPassword(String rawPassword, String passwordFromDb) {
        return passwordEncoder.matches(rawPassword, passwordFromDb);
    }

    public void changeUserPassword(String email, String password) {
        userDAO.changePassword(email, passwordEncoder.encode(password));
    }
}
