package com.tpark.back.service;

import com.tpark.back.model.School;

public interface SchoolService {
    void createSchool(School school, String email);
    School getSchoolByAdmin(String email);

    School getSchoolByStudent(String student);
}
