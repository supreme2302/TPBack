package com.tpark.back.service;

import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.model.dto.StudentDTO;

import java.io.IOException;

public interface SchoolService {
    void createSchool(String schoolName, Integer id);

    SchoolDTO getSchoolByAdmin(String email);

    SchoolDTO getSchoolByStudent(String student);

    void changeSchool(SchoolDTO schoolDTO, String user);

    String makeApp(String user) throws IOException;

    String sendMessageToUser(SchoolDTO schoolDTO, String receiver);
}
