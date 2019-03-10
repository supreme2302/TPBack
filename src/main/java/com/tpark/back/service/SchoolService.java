package com.tpark.back.service;

import com.tpark.back.model.School;

public interface SchoolService {
    void createSchool(School school);
    School getSchoolByAdmin(String email);
}
