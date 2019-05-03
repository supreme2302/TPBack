package com.tpark.back.dao;

import com.tpark.back.model.dto.StudentDTO;
import com.tpark.back.model.dto.StudentWithGroupsDTO;

import java.util.List;

public interface StudentDAO {

    void addStudent(StudentDTO studentDTO,String admin);

    StudentDTO getStudentByEmailWithGroupId(String email);

    StudentDTO getStudentByEmailWithoutGroupId(String email);

    List<StudentDTO> getStudentsFromGroupById(int id);

    List<StudentDTO> getAllStudents(String admin);

    void deleteStudent(Integer id, String admin);

    void changeStudent(StudentDTO studentDTO, String email);

    StudentWithGroupsDTO getStudentByEmailWithGroups(String email);

    void changePassword(StudentDTO studentDTO);
}
