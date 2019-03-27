package com.tpark.back.service;

import com.tpark.back.model.Admin;

public interface AdminService {

    Admin getAdminByEmail(String email);

    void addAdmin(Admin admin);

    boolean checkAdminPassword(String rawPassword, String passwordFromDb);

    void changeAdminPassword(String email, String password);

    void addNewAdmin(String toString, Admin admin);
}
