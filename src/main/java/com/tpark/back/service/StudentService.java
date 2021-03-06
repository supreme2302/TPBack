package com.tpark.back.service;

import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.StudentDTO;
import com.tpark.back.model.dto.StudentWithGroupsDTO;

import java.util.List;

public interface StudentService {

    void addStudent(StudentDTO studentDTO, String admin);

    boolean checkStudent(String rawPassword, String passwordFromDb);

    StudentDTO getStudentByEmailWithGroupId(String email);
    StudentDTO getStudentByEmailWithoutGroupId(String email);

    List<StudentDTO> getStudentsFromGroupById(int id);

    List<StudentDTO> getAllStudents(String admin);

    void deleteStudent(Integer id, String toString);

    void changeStudent(StudentDTO studentDTO, String email);

    StudentWithGroupsDTO getStudentByEmailWithGroups(String email);

    void sendWelcomeMessageToUser(AdminDTO sender, StudentDTO receiver);

    void sendRestoreMessageToUser(StudentDTO receiver);

    void changePassword(StudentDTO studentDTO, String adminEmail);
}
