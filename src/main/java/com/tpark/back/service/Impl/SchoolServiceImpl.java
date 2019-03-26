package com.tpark.back.service.Impl;

import com.tpark.back.dao.Impl.SchoolDAOImpl;
import com.tpark.back.dao.SchoolDAO;
import com.tpark.back.model.School;
import com.tpark.back.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolDAO schoolDAO;

    @Autowired
    SchoolServiceImpl(SchoolDAO schoolDAO){
        this.schoolDAO = schoolDAO;
    }

    @Override
    public void createSchool(School school) {
        
        schoolDAO.createSchool(school);
    }

    @Override
    public School getSchoolByAdmin(String email) {
        return schoolDAO.getSchoolByAdmin(email);
    }

    @Override
    public School getSchoolByStudent(String student) {
        return schoolDAO.getSchoolByStudent(student);
    }
}
