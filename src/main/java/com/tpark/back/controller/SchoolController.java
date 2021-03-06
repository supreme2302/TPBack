package com.tpark.back.controller;


import com.tpark.back.model.UserStatus;
import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.SchoolDTO;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.SchoolService;
import com.tpark.back.service.StudentService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.tpark.back.model.Resourses.PATH_SCHOOL_AVATARS_FOLDER;


@RestController
@RequestMapping("/school")
@EnableJdbcHttpSession
public class SchoolController {

    private final SchoolService schoolService;
    private final AdminService adminService;
    private final StudentService studentService;

    @Autowired
    public SchoolController(SchoolService schoolService, AdminService adminService, StudentService studentService) {
        this.schoolService = schoolService;
        this.adminService = adminService;
        this.studentService = studentService;
    }

    @GetMapping(path = "/")
    public ResponseEntity getSchool(@ApiIgnore HttpSession session) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if (session.getAttribute("student") == null || studentService.getStudentByEmailWithoutGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }

        try {
            if (session.getAttribute("user") != null) {
                SchoolDTO res = schoolService.getSchoolByAdmin(session.getAttribute("user").toString());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(res);
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(schoolService.getSchoolByStudent(session.getAttribute("student").toString()));
            }
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(@ApiIgnore HttpSession session, @RequestBody SchoolDTO schoolDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        AdminDTO user = adminService.getAdminByEmail(session.getAttribute("user").toString());
        if (user == null ||
                user.getId() !=
                        schoolService.getSchoolByAdmin(session.getAttribute("user").toString()).getAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            schoolService.changeSchool(schoolDTO, user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_CHANGED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/makeapp")
    public ResponseEntity makeapp(@ApiIgnore HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null ||
                adminService.getAdminByEmail(session.getAttribute("user").toString()).getId() !=
                        schoolService.getSchoolByAdmin(session.getAttribute("user").toString()).getAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            String adminEmail = session.getAttribute("user").toString();
            SchoolDTO schoolDTO = schoolService.getSchoolByAdmin(adminEmail);
            schoolService.makeApp(schoolDTO, adminEmail);
            String message = String.format("https://lingvomake.ml/%s.apk",
                    schoolDTO.getId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    /**
     * @param file - картиночка
     * @param id - id курса
     */
    @PostMapping("/changeAvatar")
    @SneakyThrows(IOException.class)
    public ResponseEntity changeAva(@RequestParam("image") MultipartFile file,
                                    @RequestParam("id") int id,
                                    @ApiIgnore HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not Found");
        }
        String link;
        link = schoolService.store(file, id);
        return ResponseEntity.status(HttpStatus.OK).body(link);
    }

    @SuppressWarnings("Duplicates")
    @GetMapping(path = "/image/{imageName}")
    public ResponseEntity getImageByEmail(@PathVariable("imageName") String imageName) throws IOException {
        BufferedImage file;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            file = ImageIO.read(new File(PATH_SCHOOL_AVATARS_FOLDER + imageName + ".jpg"));
        } catch (IIOException e) {
            file = ImageIO.read(new File(PATH_SCHOOL_AVATARS_FOLDER + "default_course.jpg"));
        }

        ImageIO.write(file, "png", bao);
        return ResponseEntity.ok(bao.toByteArray());
    }
}
