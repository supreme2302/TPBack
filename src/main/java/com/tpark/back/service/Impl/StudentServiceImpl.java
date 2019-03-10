package com.tpark.back.service.Impl;

import com.tpark.back.dao.StudentDAO;
import com.tpark.back.model.Student;
import com.tpark.back.service.StudentService;
import com.tpark.back.util.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
