package com.tpark.back.service;

import com.tpark.back.model.Student;

public interface StudentService {

    void addStudent(Student student);

    boolean checkStudent(String rawPassword, String passwordFromDb);

    Student getStudentByEmailWithoutGroupId(String email);
}
