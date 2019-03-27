package com.tpark.back.service;

import com.tpark.back.model.Student;

import java.util.List;

public interface StudentService {

    void addStudent(Student student);

    boolean checkStudent(String rawPassword, String passwordFromDb);

    Student getStudentByEmailWithoutGroupId(String email);

    List<Student> getStudentsFromGroupById(int id);
}
