package com.tpark.back.dao;

import com.tpark.back.model.School;

public interface SchoolDAO {
    void createSchool(School school, String email);
    School getSchoolByAdmin(String admin);

    School getSchoolByStudent(String student);
}
