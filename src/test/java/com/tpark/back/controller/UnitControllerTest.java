package com.tpark.back.controller;


import com.google.gson.Gson;
import com.tpark.back.model.Group;
import com.tpark.back.model.Unit;
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

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@SpringBootTest
@Sql(value = {"/db/migration/test/V1__test-set-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void getNoneUnitsTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "exist@e.ru");
        this.mockMvc.perform(get("/unit/1")
                .contentType(contentType)
                .session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUnitTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "exist@e.ru");
        this.mockMvc.perform(get("/unit/find/1")
                .contentType(contentType)
                .session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getNoneUnitTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "exist@e.ru");
        this.mockMvc.perform(get("/unit/find/100")
                .contentType(contentType)
                .session(session))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUnitTest() throws Exception {
        Unit unit = new Unit();
        unit.setUnit_name("Fuckji");
        unit.setCourse_id(1);
        unit.setPosition(2);
        String authJSON = gson.toJson(unit);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "exist@e.ru");
        this.mockMvc.perform(post("/unit/create")
                .contentType(contentType)
                .content(authJSON)
                .session(session))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteUnitTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "exist@e.ru");
        this.mockMvc.perform(post("/unit/delete/")
                .contentType(contentType)
                .content("1")
                .session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //TODO: Add table, to make units independent from courses
}
