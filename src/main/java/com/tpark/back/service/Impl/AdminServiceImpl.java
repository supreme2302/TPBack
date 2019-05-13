package com.tpark.back.service.Impl;

import com.tpark.back.dao.AdminDAO;
import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.ChangePasswordDTO;
import com.tpark.back.model.dto.IdDTO;
import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.MailSender;
import com.tpark.back.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDAO adminDAO;
    private final PasswordEncoder passwordEncoder;
    private final SchoolService schoolService;
    private final MailSender mailSender;

    @Autowired
    public AdminServiceImpl(AdminDAO adminDAO, PasswordEncoder passwordEncoder,
                            SchoolService schoolService, MailSender mailSender) {
        this.adminDAO = adminDAO;
        this.passwordEncoder = passwordEncoder;
        this.schoolService = schoolService;
        this.mailSender = mailSender;
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
        adminDTO.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        adminDAO.addNewAdmin(toString, adminDTO);
    }

    @Override
    public List<AdminDTO> getSchoolAdmins(AdminDTO user) {
        return adminDAO.getSchoolAdmins(user);
    }

    @Override
    public void deleteAdmin(IdDTO idDTO, AdminDTO userFromDb) {
        adminDAO.deleteAdmin(idDTO, userFromDb);
    }

    @Override
    @Async
    public void sendWelcomeMessageToAdmin(AdminDTO sender, AdminDTO receiver) {
        SchoolDTO school = schoolService.getSchoolByAdmin(sender.getEmail());
        String message = String.format(
                "Hello! \n" +
                        "Welcome to %s! \n Your login: %s \n Your password: %s",
                school.getName(),
                receiver.getEmail(),
                receiver.getPassword()
        );
        mailSender.send(receiver.getEmail(), "Welcome to " + school.getName(), message);

    }

    @Override
    public void changeTeacherPassword(int schoolId, ChangePasswordDTO newPassword) {
        newPassword.setNewPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        adminDAO.changeTeacher(schoolId, newPassword);
    }
}
