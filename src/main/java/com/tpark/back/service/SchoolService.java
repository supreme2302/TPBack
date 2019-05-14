package com.tpark.back.service;

import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.SchoolDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SchoolService {
    void createSchool(String schoolName, Integer id);

    SchoolDTO getSchoolByAdmin(String email);

    SchoolDTO getSchoolByStudent(String student);

    void changeSchool(SchoolDTO schoolDTO, AdminDTO user);

    void makeApp(SchoolDTO schoolDTO, String email) throws IOException;

    void sendMessageToUser(SchoolDTO schoolDTO, String receiver);

    String store(MultipartFile file, int id) throws IOException;
}
