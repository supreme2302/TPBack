package com.tpark.back.service.Impl;

import com.tpark.back.dao.StudentDAO;
import com.tpark.back.model.dto.StudentDTO;
import com.tpark.back.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(StudentDAO studentDAO, PasswordEncoder passwordEncoder) {
        this.studentDAO = studentDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addStudent(StudentDTO studentDTO, String admin) {
        studentDTO.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        studentDAO.addStudent(studentDTO,admin);
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
}
