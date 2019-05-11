package com.tpark.back.dao;

import com.tpark.back.model.dto.SchoolDTO;

public interface SchoolDAO {
    void createSchool(String schoolName, int id);

    SchoolDTO getSchoolByAdmin(String admin);

    SchoolDTO getSchoolByStudent(String student);

    void changeSchool(SchoolDTO school, String user);

    void savePicture(String link, int id);
}
