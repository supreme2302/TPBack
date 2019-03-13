package com.tpark.back.controller;


import com.google.gson.Gson;
import com.tpark.back.model.Admin;
import com.tpark.back.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@SpringBootTest
@Sql(value = {"/db/migration/test/test_session.sql",
        "/db/migration/test/V1__test-set-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class StudentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void createStudentTestOk() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("exist@e.ru");
        Student student = new Student();
        student.setName("test_name");
        student.setSurname("test_surname");
        student.setEmail("test_student@e.ru");
        student.setSchool_id(1);
        this.mockMvc.perform(post("/student/create")
                .contentType(contentType)
                .content(gson.toJson(student))
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void authStudentTestOk() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("exist@e.ru");
        Student student = new Student();
        student.setName("test_name");
        student.setSurname("test_surname");
        student.setEmail("test_student@e.ru");
        student.setSchool_id(1);
        String response = this.mockMvc.perform(post("/student/create")
                .contentType(contentType)
                .content(gson.toJson(student))
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        Student element = gson.fromJson (response, Student.class);
        Student authStudent = new Student();
        authStudent.setEmail(element.getEmail());
        authStudent.setPassword(element.getPassword());
        this.mockMvc.perform(post("/student/auth")
                .contentType(contentType)
                .content(gson.toJson(authStudent)))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void authStudentTestForbidden() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getStudentCookie("a@a.ru");
        Student student = new Student();
        this.mockMvc.perform(post("/student/auth")
                .contentType(contentType)
                .cookie(allCookies)
                .content(gson.toJson(student)))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void authStudentTestNotFound() throws Exception {
        Student student = new Student();
        student.setEmail("notexitstemail@e.ru");
        this.mockMvc.perform(post("/student/auth")
                .contentType(contentType)
                .content(gson.toJson(student)))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void authStudentTestWrongCredentials() throws Exception {
        Admin admin = new Admin();
        admin.setEmail("exist@e.ru");
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("exist@e.ru");
        Student student = new Student();
        student.setName("test_name");
        student.setSurname("test_surname");
        student.setEmail("test_student@e.ru");
        student.setSchool_id(1);
        this.mockMvc.perform(post("/student/create")
                .contentType(contentType)
                .content(gson.toJson(student))
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isCreated());

        Student authStudent = new Student();
        authStudent.setEmail("test_student@e.ru");
        authStudent.setPassword("000wrongpassword");
        this.mockMvc.perform(post("/student/auth")
                .contentType(contentType)
                .content(gson.toJson(authStudent)))
                .andDo(print()).andExpect(status().isBadRequest());
    }
}
