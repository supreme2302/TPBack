package com.tpark.back.controller;

import com.google.gson.Gson;
import com.tpark.back.model.Admin;
import com.tpark.back.model.Student;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class CookieAssistant {


    private MockMvc mockMvc;

    private Gson gson = new Gson();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    CookieAssistant(MockMvc mock){
        this.mockMvc = mock;
    }

    Cookie[] getAdminCookie(String email) throws Exception {
        Admin user = new Admin();
        user.setEmail(email);
        user.setPassword("123");
        Cookie[] cook = this.mockMvc.perform(post("/admin/auth")
                .contentType(contentType)
                .content(gson.toJson(user)))
                .andReturn()
                .getResponse()
                .getCookies();
        return cook;
    }

    Cookie[] getStudentCookie(String email) throws Exception {
        Student user = new Student();
        user.setEmail(email);
        user.setPassword("123");
        Cookie[] cook = this.mockMvc.perform(post("/student/auth")
                .contentType(contentType)
                .content(gson.toJson(user)))
                .andReturn()
                .getResponse()
                .getCookies();
        return cook;
    }

}
