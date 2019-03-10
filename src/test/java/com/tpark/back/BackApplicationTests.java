package com.tpark.back;

import com.google.gson.Gson;
import com.tpark.back.model.ChangePassword;
import com.tpark.back.model.Admin;
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
public class BackApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    @Test
    public void signUpTest() throws Exception {
        Admin admin = new Admin();
        admin.setEmail("test@s.ru");
        admin.setPassword("123");
        String authJSON = gson.toJson(admin);
        this.mockMvc.perform(post("/admin/register")
                .contentType(contentType)
                .content(authJSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void signInOkTest() throws Exception {
        Admin user = new Admin();
        user.setEmail("exist@e.ru");
        user.setPassword("123");
        this.mockMvc.perform(post("/admin/auth")
                .contentType(contentType)
                .content(gson.toJson(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void logoutAccessErrorTest() throws Exception {
        this.mockMvc.perform(post("/admin/logout")
                .contentType(contentType))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void logoutOkTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "exist@e.ru");
        this.mockMvc.perform(post("/admin/logout")
                .contentType(contentType)
                .session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void changePasswordOkTest() throws Exception {
        Admin user = new Admin();
        user.setEmail("exist@e.ru");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user.getEmail());
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword("123");
        changePassword.setNewPassword("321");
        this.mockMvc.perform(post("/admin/change")
                .contentType(contentType)
                .content(gson.toJson(changePassword))
                .session(session))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/admin/logout")
                .contentType(contentType)
                .session(session))
                .andDo(print())
                .andExpect(status().isOk());
        user.setPassword("321");
        this.mockMvc.perform(post("/admin/auth")
                .contentType(contentType)
                .content(gson.toJson(user)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
