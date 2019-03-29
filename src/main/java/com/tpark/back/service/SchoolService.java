package com.tpark.back.service;

import com.tpark.back.model.dto.SchoolDTO;

public interface SchoolService {
    void createSchool(String schoolName, String email);

    SchoolDTO getSchoolByAdmin(String email);

    SchoolDTO getSchoolByStudent(String student);
}
