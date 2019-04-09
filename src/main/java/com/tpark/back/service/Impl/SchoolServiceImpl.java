package com.tpark.back.service.Impl;

import com.tpark.back.dao.AdminDAO;
import com.tpark.back.dao.SchoolDAO;
import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.model.exception.NotFoundException;
import com.tpark.back.service.SchoolService;
import io.swagger.models.auth.In;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

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
    public void createSchool(String schoolName, Integer id) {
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

    @Override
    public void changeSchool(SchoolDTO school, String user) {
        schoolDAO.changeSchool(school, user);
    }

    @Override
    public void makeApp(String user) throws IOException {
        SchoolDTO schoolDTO = schoolDAO.getSchoolByAdmin(user);


        ProcessBuilder pb = new ProcessBuilder("src/main/resources/scripts/build.sh", Integer.toString(schoolDTO.getId()), schoolDTO.getMain_color(),
                schoolDTO.getSecondary_color(), schoolDTO.getName(), schoolDTO.getLanguage());
        Process p = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
