package com.tpark.back.dao;

import com.tpark.back.model.dto.AdminDTO;

public interface AdminDAO {
    AdminDTO getAdminByEmail(String email);

    void addAdmin(AdminDTO user);

    void addNewAdmin(String toString, AdminDTO adminDTO);

    void changePassword(String email, String encode);

    Integer getAdminIdByEmail(String email);
}
