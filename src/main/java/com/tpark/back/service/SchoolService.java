package com.tpark.back.service;

import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.model.dto.StudentDTO;

import java.io.IOException;
import java.util.concurrent.Future;

public interface SchoolService {
    void createSchool(String schoolName, Integer id);

    SchoolDTO getSchoolByAdmin(String email);

    SchoolDTO getSchoolByStudent(String student);

    void changeSchool(SchoolDTO schoolDTO, String user);

    void makeApp(SchoolDTO schoolDTO) throws IOException;

    void sendMessageToUser(SchoolDTO schoolDTO, String receiver);
}
