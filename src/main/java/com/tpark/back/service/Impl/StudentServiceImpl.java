package com.tpark.back.service.Impl;

import com.tpark.back.dao.StudentDAO;
import com.tpark.back.model.Student;
import com.tpark.back.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    
    private final StudentDAO studentDAO;
    
    @Autowired
    public StudentServiceImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }
    
    @Override
    public void addStudent(Student student) {
        studentDAO.addStudent(student);
    }
}
