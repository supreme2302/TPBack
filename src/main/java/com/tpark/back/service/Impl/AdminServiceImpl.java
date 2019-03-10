package com.tpark.back.service.Impl;

import com.tpark.back.dao.Impl.AdminDAOImpl;
import com.tpark.back.model.Admin;
import com.tpark.back.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDAOImpl adminDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(AdminDAOImpl adminDAO, PasswordEncoder passwordEncoder) {
        this.adminDAO = adminDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin getUserByEmail(String email) {
        return adminDAO.getAdminByEmail(email);
    }


    @Override
    public void addUser(Admin user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        adminDAO.addAdmin(user);
    }

    @Override
    public boolean checkAdminPassword(String rawPassword, String passwordFromDb) {
        return passwordEncoder.matches(rawPassword, passwordFromDb);
    }

    @Override
    public void changeAdminPassword(String email, String password) {
        adminDAO.changePassword(email, passwordEncoder.encode(password));
    }
}
