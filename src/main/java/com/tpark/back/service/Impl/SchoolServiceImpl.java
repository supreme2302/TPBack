package com.tpark.back.service.Impl;

import com.tpark.back.dao.SchoolDAO;
import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.service.MailSender;
import com.tpark.back.service.SchoolService;
import com.tpark.back.util.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.tpark.back.model.Resourses.PATH_SCHOOL_AVATARS_FOLDER;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolDAO schoolDAO;
    private final MailSender mailSender;
    private final Logger logger = LoggerFactory.getLogger(SchoolServiceImpl.class);

    @Autowired
    SchoolServiceImpl(SchoolDAO schoolDAO, MailSender mailSender) {
        this.schoolDAO = schoolDAO;
        this.mailSender = mailSender;
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
    public void changeSchool(SchoolDTO school, AdminDTO user) {
        schoolDAO.changeSchool(school, user);
    }

    @Override
    @Async
    public void makeApp(SchoolDTO schoolDTO, String email) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("src/main/resources/scripts/build.sh", Integer.toString(schoolDTO.getId()), schoolDTO.getMain_color(),
                schoolDTO.getSecondary_color(), schoolDTO.getName(), schoolDTO.getLanguage(), schoolDTO.getImageLink(),schoolDTO.getAppName());
        Process p = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            logger.info(line);
        }
        sendMessageToUser(schoolDTO, email);
    }

    @Override
    public void sendMessageToUser(SchoolDTO schoolDTO, String email) {
        String message = String.format(
                "Welcome to lingvomake! Link to download the application" +
                        "\nhttps://lingvomake.ml/%s.apk",
                schoolDTO.getId()

        );
        logger.info("sending............");
        logger.info("email " + email);
        mailSender.send(email, "Welcome to " + schoolDTO.getName(), message);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public String store(MultipartFile file, int id) throws IOException {
        String link = String.valueOf(id) + "." + RandomString.getRandomString() + ".jpg";
        File tosave = new File(PATH_SCHOOL_AVATARS_FOLDER + link);
        file.transferTo(tosave);
        schoolDAO.savePicture(link, id);
        return link;
    }
}
