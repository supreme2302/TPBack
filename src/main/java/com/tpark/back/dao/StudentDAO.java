package com.tpark.back.dao;

import com.tpark.back.model.dto.StudentDTO;

import java.util.List;

public interface StudentDAO {

    void addStudent(StudentDTO studentDTO);

    StudentDTO getStudentByEmailWithGroupId(String email);

    StudentDTO getStudentByEmailWithoutGroupId(String email);

    List<StudentDTO> getStudentsFromGroupById(int id);

    List<StudentDTO> getAllStudents(String admin);
}
