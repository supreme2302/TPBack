package com.tpark.back.service.Impl;

import com.tpark.back.dao.AdminDAO;
import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDAO adminDAO;
    private final PasswordEncoder passwordEncoder;
    private final SchoolService schoolService;

    @Autowired
    public AdminServiceImpl(AdminDAO adminDAO, PasswordEncoder passwordEncoder,
                            SchoolService schoolService) {
        this.adminDAO = adminDAO;
        this.passwordEncoder = passwordEncoder;
        this.schoolService = schoolService;
    }

    @Override
    public AdminDTO getAdminByEmail(String email) {
        return adminDAO.getAdminByEmail(email);
    }


    @Transactional
    @Override
    public void addAdminAndCreateSchool(AdminDTO adminDTO) {
        adminDTO.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        Integer adminId = adminDAO.addAdmin(adminDTO);
        schoolService.createSchool(adminDTO.getSchoolName(), adminId);
    }

    @Override
    public boolean checkAdminPassword(String rawPassword, String passwordFromDb) {
        return passwordEncoder.matches(rawPassword, passwordFromDb);
    }

    @Override
    public void changeAdminPassword(String email, String password) {
        adminDAO.changePassword(email, passwordEncoder.encode(password));
    }

    @Override
    public void addNewAdmin(String toString, AdminDTO adminDTO) {
        adminDAO.addNewAdmin(toString, adminDTO);
    }
}
