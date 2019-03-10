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

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@SpringBootTest
@Sql(value = {"/db/migration/test/V1__test-set-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

public class StudentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Test
    public void createStudentTestOk() throws Exception {
        Admin user = new Admin();
        user.setEmail("exist@e.ru");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user.getEmail());
        Student student = new Student();
        student.setName("test_name");
        student.setSurname("test_surname");
        student.setEmail("test_student@e.ru");
        student.setSchool_id(1);
    }
}
