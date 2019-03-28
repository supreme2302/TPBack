package com.tpark.back.controller;

import com.google.gson.Gson;
import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.StudentDTO;
import org.springframework.http.MediaType;
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
        AdminDTO user = new AdminDTO();
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
        StudentDTO user = new StudentDTO();
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
