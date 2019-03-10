package com.tpark.back.service.Impl;

import com.tpark.back.model.School;
import com.tpark.back.service.SchoolService;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl implements SchoolService {
    @Override
    public void createSchool(School school) {

    }

    @Override
    public School getSchoolByAdmin(String email) {
        return null;
    }
}
