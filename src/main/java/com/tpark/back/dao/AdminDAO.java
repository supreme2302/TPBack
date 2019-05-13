package com.tpark.back.dao;

import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.ChangePasswordDTO;
import com.tpark.back.model.dto.IdDTO;
import sun.security.util.Password;

import java.util.List;

public interface AdminDAO {
    AdminDTO getAdminByEmail(String email);

    Integer addAdmin(AdminDTO user);

    void addNewAdmin(String toString, AdminDTO adminDTO);

    void changePassword(Integer schoolId, ChangePasswordDTO pass);

    Integer getAdminIdByEmail(String email);

    List<AdminDTO> getSchoolAdmins(AdminDTO user);

    void deleteAdmin(IdDTO idDTO, AdminDTO userFromDb);
}
