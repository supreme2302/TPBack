package com.tpark.back.controller;


import com.google.gson.Gson;
import com.tpark.back.config.EmbeddedPostgresConfiguration;
import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.StudentDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EmbeddedPostgresConfiguration.class, MockMvcAutoConfiguration.class})
@TestPropertySource("/application-test.properties")
@Sql(value = {"/db/migration/test/test_session.sql",
        "/db/migration/test/test-set-before.sql"},
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
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName("test_name");
        studentDTO.setSurname("test_surname");
        studentDTO.setEmail("test_student@e.ru");
        studentDTO.setSchool_id(1);
        this.mockMvc.perform(post("/studentDTO/create")
                .contentType(contentType)
                .content(gson.toJson(studentDTO))
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void authStudentTestOk() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("exist@e.ru");
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName("test_name");
        studentDTO.setSurname("test_surname");
        studentDTO.setEmail("test_student@e.ru");
        studentDTO.setSchool_id(1);
        String response = this.mockMvc.perform(post("/studentDTO/create")
                .contentType(contentType)
                .content(gson.toJson(studentDTO))
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        StudentDTO element = gson.fromJson (response, StudentDTO.class);
        StudentDTO authStudent = new StudentDTO();
        authStudent.setEmail(element.getEmail());
        authStudent.setPassword(element.getPassword());
        this.mockMvc.perform(post("/studentDTO/auth")
                .contentType(contentType)
                .content(gson.toJson(authStudent)))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void authStudentTestForbidden() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getStudentCookie("a@a.ru");
        StudentDTO studentDTO = new StudentDTO();
        this.mockMvc.perform(post("/studentDTO/auth")
                .contentType(contentType)
                .cookie(allCookies)
                .content(gson.toJson(studentDTO)))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void authStudentTestNotFound() throws Exception {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setEmail("notexitstemail@e.ru");
        this.mockMvc.perform(post("/studentDTO/auth")
                .contentType(contentType)
                .content(gson.toJson(studentDTO)))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void authStudentTestWrongCredentials() throws Exception {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setEmail("exist@e.ru");
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("exist@e.ru");
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName("test_name");
        studentDTO.setSurname("test_surname");
        studentDTO.setEmail("test_student@e.ru");
        studentDTO.setSchool_id(1);
        this.mockMvc.perform(post("/studentDTO/create")
                .contentType(contentType)
                .content(gson.toJson(studentDTO))
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isCreated());

        StudentDTO authStudent = new StudentDTO();
        authStudent.setEmail("test_student@e.ru");
        authStudent.setPassword("000wrongpassword");
        this.mockMvc.perform(post("/studentDTO/auth")
                .contentType(contentType)
                .content(gson.toJson(authStudent)))
                .andDo(print()).andExpect(status().isBadRequest());
    }
}
