package com.tpark.back.controller;


import com.google.gson.Gson;
import com.tpark.back.config.EmbeddedPostgresConfiguration;
import com.tpark.back.model.dto.GroupDTO;
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
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void getNoneGroupsTest() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("exist@e.ru");
        this.mockMvc.perform(get("/group/1")
                .contentType(contentType)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getGroupTest() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("existforkostyan@e.ru");
        this.mockMvc.perform(get("/group/find/1")
                .contentType(contentType)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getGroupsStudentTest() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getStudentCookie("student@mail.ru");
        this.mockMvc.perform(get("/group/")
                .contentType(contentType)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getGroupStudentTest() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getStudentCookie("student@mail.ru");
        this.mockMvc.perform(get("/group/find/1")
                .contentType(contentType)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getNoneGroupTest() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("existforkostyan@e.ru");
        this.mockMvc.perform(get("/group/find/100")
                .contentType(contentType)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void createGroupTest() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("Fuckji");
        groupDTO.setCourse_id(1);
        groupDTO.setCurr_unit(1);
        String authJSON = gson.toJson(groupDTO);
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("existforkostyan@e.ru");
        this.mockMvc.perform(post("/groupDTO/create")
                .contentType(contentType)
                .content(authJSON)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteGroupTest() throws Exception {
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("existforkostyan@e.ru");
        this.mockMvc.perform(post("/group/delete/")
                .contentType(contentType)
                .content("1")
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
