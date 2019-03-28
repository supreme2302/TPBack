package com.tpark.back.service;

import com.tpark.back.model.dto.StudentDTO;

import java.util.List;

public interface StudentService {

    void addStudent(StudentDTO studentDTO);

    boolean checkStudent(String rawPassword, String passwordFromDb);

    StudentDTO getStudentByEmailWithGroupId(String email);
    StudentDTO getStudentByEmailWithoutGroupId(String email);

    List<StudentDTO> getStudentsFromGroupById(int id);

    List<StudentDTO> getAllStudents(String admin);
}
