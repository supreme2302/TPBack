package com.tpark.back.service;

import com.tpark.back.model.Admin;

public interface AdminService {

    Admin getUserByEmail(String email);

    void addUser(Admin user);

    boolean checkAdminPassword(String rawPassword, String passwordFromDb);

    void changeAdminPassword(String email, String password);
}
