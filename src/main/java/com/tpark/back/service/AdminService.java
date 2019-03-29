package com.tpark.back.service;

import com.tpark.back.model.dto.AdminDTO;

public interface AdminService {

    AdminDTO getAdminByEmail(String email);

    void addAdminAndCreateSchool(AdminDTO adminDTO);

    boolean checkAdminPassword(String rawPassword, String passwordFromDb);

    void changeAdminPassword(String email, String password);

    void addNewAdmin(String toString, AdminDTO adminDTO);
}
