package com.tpark.back.service.Impl;

import com.tpark.back.dao.Impl.SchoolIDDAO;
import com.tpark.back.dao.StudentDAO;
import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.model.dto.StudentDTO;
import com.tpark.back.model.dto.StudentWithGroupsDTO;
import com.tpark.back.service.MailSender;
import com.tpark.back.service.SchoolService;
import com.tpark.back.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;
    private final PasswordEncoder passwordEncoder;
    private final SchoolService schoolService;
    private final SchoolIDDAO schoolIDDAO;
    private final MailSender mailSender;

    @Autowired
    public StudentServiceImpl(StudentDAO studentDAO,
                              PasswordEncoder passwordEncoder,
                              SchoolService schoolService,
                              MailSender mailSender,
                              SchoolIDDAO schoolIDDAO) {
        this.studentDAO = studentDAO;
        this.passwordEncoder = passwordEncoder;
        this.schoolService = schoolService;
        this.mailSender = mailSender;
        this.schoolIDDAO = schoolIDDAO;
    }

    @Override
    public void addStudent(StudentDTO studentDTO, String admin) {
        studentDTO.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        studentDAO.addStudent(studentDTO, admin);
    }

    @Override
    public boolean checkStudent(String rawPassword, String passwordFromDb) {
        return passwordEncoder.matches(rawPassword, passwordFromDb);
    }

    @Override
    public StudentDTO getStudentByEmailWithoutGroupId(String email) {
        return studentDAO.getStudentByEmailWithoutGroupId(email);
    }

    @Override
    public StudentDTO getStudentByEmailWithGroupId(String email) {
        return studentDAO.getStudentByEmailWithGroupId(email);
    }

    @Override
    public List<StudentDTO> getStudentsFromGroupById(int id) {
        return studentDAO.getStudentsFromGroupById(id);
    }

    @Override
    public List<StudentDTO> getAllStudents(String admin){
        return studentDAO.getAllStudents(admin);

    }

    @Override
    public void deleteStudent(Integer id, String admin) {
        studentDAO.deleteStudent(id,admin);
    }

    @Override
    public void changeStudent(StudentDTO studentDTO, String email) {
        studentDAO.changeStudent(studentDTO, email);
    }

    @Override
    public StudentWithGroupsDTO getStudentByEmailWithGroups(String email) {
        return studentDAO.getStudentByEmailWithGroups(email);
    }

    @Override
    @Async
    public void sendWelcomeMessageToUser(AdminDTO sender, StudentDTO receiver) {
        SchoolDTO school = schoolService.getSchoolByAdmin(sender.getEmail());
        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to %s! \n Your login: %s \n Your password: %s",
                receiver.getName(),
                school.getName(),
                receiver.getEmail(),
                receiver.getPassword()
        );
        mailSender.send(receiver.getEmail(), "Welcome to " + school.getName(), message);
    }

    @Override
    @Async
    public void sendRestoreMessageToUser(StudentDTO receiver) {
        String message = String.format(
                "Your password has been reset.\n" +
                        "Your new password: %s",
                receiver.getPassword()
        );
        mailSender.send(receiver.getEmail(), "Password recovery", message);
    }

    @Override
    public void changePassword(StudentDTO studentDTO, String adminEmail) {
        Integer school_id = schoolIDDAO.getSchoolId(adminEmail);
        studentDTO.setSchool_id(school_id);
        studentDAO.changePassword(studentDTO);
    }
}
