package com.tpark.back.service.Impl;

import com.tpark.back.dao.StudentDAO;
import com.tpark.back.model.Student;
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
    public void addStudent(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentDAO.addStudent(student);
    }

    @Override
    public boolean checkStudent(String rawPassword, String passwordFromDb) {
        return passwordEncoder.matches(rawPassword, passwordFromDb);
    }

    @Override
    public Student getStudentByEmailWithoutGroupId(String email) {
        return studentDAO.getStudentByEmailWithoutGroupId(email);
    }

    @Override
    public Student getStudentByEmailWithGroupId(String email) {
        return studentDAO.getStudentByEmailWithGroupId(email);
    }

    @Override
    public List<Student> getStudentsFromGroupById(int id) {
        return studentDAO.getStudentsFromGroupById(id);
    }
}
