package com.tpark.back.controller;

import com.tpark.back.model.dto.*;
import com.tpark.back.model.UserStatus;
import com.tpark.back.service.AdminService;
import com.tpark.back.service.StudentService;
import com.tpark.back.util.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

@SuppressWarnings("Duplicates")
@RestController
@RequestMapping(path = "/student")
@EnableJdbcHttpSession
public class StudentController {

    private final StudentService studentService;
    private final AdminService adminService;

    @Autowired
    public StudentController(StudentService studentService, AdminService adminService) {
        this.studentService = studentService;
        this.adminService = adminService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity createStudent(@ApiIgnore HttpSession session, @RequestBody StudentDTO studentDTO) {
        Object adminSession = session.getAttribute("user");
        if (adminSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserStatus.ACCESS_ERROR);
        }

        AdminDTO existingAdmin = adminService.getAdminByEmail(adminSession.toString());

        if (existingAdmin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ACCESS_ERROR);
        }

        String password = RandomString.getRandomString();
        studentDTO.setPassword(password);
        try {
            studentService.addStudent(studentDTO, adminSession.toString());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.NOT_UNIQUE_FIELDS_IN_REQUEST);
        }
        studentDTO.setPassword(password);
        StudentDTO toMail = new StudentDTO();
        toMail.setEmail(studentDTO.getEmail());
        toMail.setPassword(studentDTO.getPassword());
        toMail.setName(studentDTO.getName());
        studentService.sendWelcomeMessageToUser(existingAdmin, toMail);
        studentDTO.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }


    @PostMapping(path = "/change")
    public ResponseEntity changeStudent(@ApiIgnore HttpSession session, @RequestBody StudentDTO studentDTO) {
        Object adminSession = session.getAttribute("user");
        if (adminSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserStatus.ACCESS_ERROR);
        }

        AdminDTO existingAdmin = adminService.getAdminByEmail(adminSession.toString());

        if (existingAdmin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ACCESS_ERROR);
        }

        try {
            studentService.changeStudent(studentDTO, adminSession.toString());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.NOT_UNIQUE_FIELDS_IN_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }

    @PostMapping(path = "/reset")
    public ResponseEntity resetPassword(@ApiIgnore HttpSession session,
                                        @RequestBody StudentDTO studentDTO) {
        Object adminSession = session.getAttribute("user");
        if (adminSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserStatus.ACCESS_ERROR);
        }

        AdminDTO existingAdmin = adminService.getAdminByEmail(adminSession.toString());

        if (existingAdmin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ACCESS_ERROR);
        }
        String newPassword = RandomString.getRandomString();
        studentDTO.setPassword(newPassword);
        studentService.changePassword(studentDTO, adminSession.toString());
        studentDTO.setPassword(newPassword);
        StudentDTO toMail = new StudentDTO();
        toMail.setEmail(studentDTO.getEmail());
        toMail.setPassword(studentDTO.getPassword());
        toMail.setName(studentDTO.getName());
        studentService.sendRestoreMessageToUser(toMail);
        return ResponseEntity.status(HttpStatus.OK).body(UserStatus.SUCCESSFULLY_CHANGED);
    }

    @PostMapping(path = "/delete")
    public ResponseEntity deleteStudent(@ApiIgnore HttpSession session, @RequestBody IdDTO idDTO) {
        Object adminSession = session.getAttribute("user");
        if (adminSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserStatus.ACCESS_ERROR);
        }

        AdminDTO existingAdmin = adminService.getAdminByEmail(adminSession.toString());

        if (existingAdmin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ACCESS_ERROR);
        }

        try {
            studentService.deleteStudent(idDTO.getId(), adminSession.toString());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(UserStatus.NOT_UNIQUE_FIELDS_IN_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserStatus.SUCCESSFULLY_CHANGED);
    }

    @PostMapping(path = "/auth")
    public ResponseEntity authStudent(@ApiIgnore HttpSession session, @RequestBody StudentAuthDTO student) {
        Object studentSession = session.getAttribute("student");
        if (studentSession != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UserStatus.ALREADY_AUTHENTICATED);
        }
        Object adminSession = session.getAttribute("user");
        if (adminSession != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ALREADY_AUTHENTICATED);
        }

        StudentWithGroupsDTO studentDTOFromDb = studentService.getStudentByEmailWithGroups(student.getEmail());

        if (studentDTOFromDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(UserStatus.NOT_FOUND);
        }

        boolean valid = studentService.checkStudent(
                student.getPassword(),
                studentDTOFromDb.getPassword()
        );

        if (!valid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(UserStatus.WRONG_CREDENTIALS);
        }

        sessionAuth(session, student.getEmail());
        studentDTOFromDb.setPassword("fuck you");
        return ResponseEntity.ok(studentDTOFromDb);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity logout(@ApiIgnore HttpSession httpSession) {
        Object object = httpSession.getAttribute("student");
        if (object == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserStatus.ACCESS_ERROR);
        }
        httpSession.invalidate();
        return ResponseEntity.ok(UserStatus.SUCCESSFULLY_LOGGED_OUT);
    }

    @GetMapping(path = "/group/{id}")
    public ResponseEntity getStudentsFromGroup(@PathVariable(name = "id") int id) {
        List<StudentDTO> studentDTOS = studentService.getStudentsFromGroupById(id);
        return ResponseEntity.ok(studentDTOS);
    }


    @GetMapping(path = "/")
    public ResponseEntity getAllStudents(@ApiIgnore HttpSession session) {
        System.out.println("students");
        Object adminSession = session.getAttribute("user");
        if (adminSession == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserStatus.ACCESS_ERROR);
        }

        AdminDTO existingAdmin = adminService.getAdminByEmail(adminSession.toString());

        if (existingAdmin == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UserStatus.ACCESS_ERROR);
        }
        List<StudentDTO> studentDTOS = studentService.getAllStudents(session.getAttribute("user").toString());
        return ResponseEntity.ok(studentDTOS);
    }

    private void sessionAuth(HttpSession session, String email) {
        session.setAttribute("student", email);
        session.setMaxInactiveInterval(60*60*24);
    }
}
