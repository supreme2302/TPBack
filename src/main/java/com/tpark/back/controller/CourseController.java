package com.tpark.back.controller;

import com.tpark.back.model.dto.CourseDTO;
import com.tpark.back.model.UserStatus;
import com.tpark.back.model.dto.IdDTO;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.CourseService;
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

import static com.tpark.back.model.Resourses.PATH_COURSE_AVATARS_FOLDER;

@RestController
@RequestMapping("/course")
@EnableJdbcHttpSession
public class CourseController {

    private final CourseService courseService;
    private final AdminService adminService;
    private final StudentService studentService;

    @Autowired
    public CourseController(CourseService courseService, AdminService adminService, StudentService studentService) {
        this.courseService = courseService;
        this.adminService = adminService;
        this.studentService = studentService;
    }

    @GetMapping(path = "/{courseId}")
    public ResponseEntity getCourse(@ApiIgnore HttpSession session, @PathVariable(name = "courseId") int courseID) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        try {
            if (session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(courseService.getCourse(courseID,session.getAttribute("user").toString()));
            }   else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(courseService.getStudentCourse(courseID,session.getAttribute("student").toString()));
            }
            //TODO: Запилить хранение параметров приложения в базке, и вместе с инфой по приложению кидать сюда json
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/")
    public ResponseEntity getSchoolCourses(@ApiIgnore HttpSession session) {
        if (session.getAttribute("user") == null || adminService.getAdminByEmail(session.getAttribute("user").toString()) == null) {
            if(session.getAttribute("student") == null || studentService.getStudentByEmailWithGroupId(session.getAttribute("student").toString()) == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(UserStatus.ACCESS_ERROR);
            }
        }
        try {
            if (session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(courseService.getCoursesByAdmin(session.getAttribute("user").toString()));
            }  else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(courseService.getCoursesByStudent(session.getAttribute("student").toString()));
            }
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(@ApiIgnore HttpSession session, @RequestBody CourseDTO courseDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            courseService.createCourse(courseDTO, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(courseDTO);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/change")
    public ResponseEntity change(@ApiIgnore HttpSession session, @RequestBody CourseDTO courseDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            courseService.changeCourse(courseDTO, session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(courseDTO);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(@ApiIgnore HttpSession session, @RequestBody IdDTO idDTO) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(UserStatus.ACCESS_ERROR);
        }
        if (adminService.getAdminByEmail(session.getAttribute("user").toString()) == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ACCESS_ERROR);
        }
        try {
            courseService.deleteCourse(idDTO.getId(), session.getAttribute("user").toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(UserStatus.SUCCESSFULLY_DELETED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.ALREADY_EXISTS);
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
        link = courseService.store(file, id);
        return ResponseEntity.status(HttpStatus.OK).body(link);
    }

    @SuppressWarnings("Duplicates")
    @GetMapping(path = "/image/{imageName}")
    public ResponseEntity getImageByEmail(@PathVariable("imageName") String imageName) throws IOException {
        BufferedImage file;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            file = ImageIO.read(new File(PATH_COURSE_AVATARS_FOLDER + imageName + ".jpg"));
        } catch (IIOException e) {
            file = ImageIO.read(new File(PATH_COURSE_AVATARS_FOLDER + "default_course.jpg"));
        }

        ImageIO.write(file, "png", bao);
        return ResponseEntity.ok(bao.toByteArray());
    }

}
