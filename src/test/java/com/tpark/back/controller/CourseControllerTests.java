package com.tpark.back.controller;


import com.google.gson.Gson;
import com.tpark.back.config.EmbeddedPostgresConfiguration;
import com.tpark.back.model.dto.CourseDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EmbeddedPostgresConfiguration.class, MockMvcAutoConfiguration.class})
@TestPropertySource("/application-test.properties")
@Sql(value = {"/test_session.sql",
        "/test-set-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CourseControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void getCoursesTestAdmin() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("exist@e.ru");
        this.mockMvc.perform(get("/course/")
                .contentType(contentType)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getCoursesTestStudent() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getStudentCookie("student@mail.ru");
        this.mockMvc.perform(get("/course/")
                .contentType(contentType)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void createCourseTest() throws Exception {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("BES");
        courseDTO.setSchoolId(1);
        String authJSON = gson.toJson(courseDTO);
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("existforkostyan@e.ru");
        this.mockMvc.perform(post("/courseDTO/create")
                .contentType(contentType)
                .content(authJSON)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteCourseTest() throws Exception {
        //todo желательно проверить, что удаление действительно произошло
        String authJSON = "{id: 1}";
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("existforkostyan@e.ru");
        this.mockMvc.perform(post("/course/delete")
                .contentType(contentType)
                .content("1")
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
