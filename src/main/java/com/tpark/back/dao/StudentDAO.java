package com.tpark.back.dao;

import com.tpark.back.model.Student;

import java.util.List;

public interface StudentDAO {

    void addStudent(Student student);

    Student getStudentByEmailWithGroupId(String email);

    Student getStudentByEmailWithoutGroupId(String email);

    List<Student> getStudentsFromGroupById(int id);
}
