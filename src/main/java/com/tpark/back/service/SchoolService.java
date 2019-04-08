package com.tpark.back.service;

import com.tpark.back.model.dto.SchoolDTO;

import java.io.IOException;

public interface SchoolService {
    void createSchool(String schoolName, Integer id);

    SchoolDTO getSchoolByAdmin(String email);

    SchoolDTO getSchoolByStudent(String student);

    void changeSchool(SchoolDTO schoolDTO, String user);

    void makeApp(String user) throws IOException;
}
