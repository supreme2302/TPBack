package com.tpark.back.dao;

import com.tpark.back.model.dto.AdminDTO;

import java.util.List;

public interface AdminDAO {
    AdminDTO getAdminByEmail(String email);

    Integer addAdmin(AdminDTO user);

    void addNewAdmin(String toString, AdminDTO adminDTO);

    void changePassword(String email, String encode);

    Integer getAdminIdByEmail(String email);

    List<AdminDTO> getSchoolAdmins(AdminDTO user);
}
