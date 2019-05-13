package com.tpark.back.service;

import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.ChangePasswordDTO;
import com.tpark.back.model.dto.IdDTO;

import java.util.List;

public interface AdminService {

    AdminDTO getAdminByEmail(String email);

    void addAdminAndCreateSchool(AdminDTO adminDTO);

    boolean checkAdminPassword(String rawPassword, String passwordFromDb);

    void changeAdminPassword(String email, String password);

    void addNewAdmin(String toString, AdminDTO adminDTO);

    List<AdminDTO> getSchoolAdmins(AdminDTO user);

    void deleteAdmin(IdDTO idDTO, AdminDTO userFromDb);

    void sendWelcomeMessageToAdmin(AdminDTO user, AdminDTO adminDTO);

    void changeTeacherPassword(int schoolId, ChangePasswordDTO newPassword);
}
