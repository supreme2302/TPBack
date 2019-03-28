package com.tpark.back.service.Impl;

import com.tpark.back.dao.AdminDAO;
import com.tpark.back.dao.SchoolDAO;
import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.model.exception.NotFoundException;
import com.tpark.back.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolDAO schoolDAO;
    private final AdminDAO adminDAO;

    @Autowired
    SchoolServiceImpl(SchoolDAO schoolDAO, AdminDAO adminDAO){
        this.schoolDAO = schoolDAO;
        this.adminDAO = adminDAO;
    }

    @Override
    public void createSchool(String schoolName, String email) {
        Integer id = adminDAO.getAdminIdByEmail(email);
        if (id == null) {
            throw new NotFoundException();
        }
        schoolDAO.createSchool(schoolName, id);
    }

    @Override
    public SchoolDTO getSchoolByAdmin(String email) {
        return schoolDAO.getSchoolByAdmin(email);
    }

    @Override
    public SchoolDTO getSchoolByStudent(String student) {
        return schoolDAO.getSchoolByStudent(student);
    }
}
