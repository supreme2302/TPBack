package com.tpark.back.controller;


import com.google.gson.Gson;
import com.tpark.back.model.Course;
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
public class CourseControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void getCoursesTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "existforkostyan@e.ru");
        this.mockMvc.perform(get("/course/")
                .contentType(contentType)
                .session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void createCourseTest() throws Exception {
        Course course = new Course();
        course.setName("BES");
        course.setSchoolId(1);
        String authJSON = gson.toJson(course);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "existforkostyan@e.ru");
        this.mockMvc.perform(post("/course/create")
                .contentType(contentType)
                .content(authJSON)
                .session(session))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    public void deleteCourseTest() throws Exception {
        //todo желательно проверить, что удаление действительно произошло
        String authJSON = "{id: 1}";
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "existforkostyan@e.ru");
        this.mockMvc.perform(post("/course/delete")
                .contentType(contentType)
                .content("1")
                .session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
