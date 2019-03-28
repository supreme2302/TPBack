package com.tpark.back.controller;

import com.google.gson.Gson;
import com.tpark.back.config.EmbeddedPostgresConfiguration;
import com.tpark.back.model.dto.AdminDTO;
import com.tpark.back.model.dto.ChangePasswordDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
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
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;



    private Gson gson = new Gson();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    @Test
    public void signUpTest() throws Exception {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setEmail("test@s.ru");
        adminDTO.setPassword("123");
        String authJSON = gson.toJson(adminDTO);
        this.mockMvc.perform(post("/adminDTO/register")
                .contentType(contentType)
                .content(authJSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void signInOkTest() throws Exception {
        AdminDTO user = new AdminDTO();
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

        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("exist@e.ru");
        this.mockMvc.perform(post("/admin/logout")
                .contentType(contentType)
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void changePasswordOkTest() throws Exception {
        AdminDTO user = new AdminDTO();
        user.setEmail("exist@e.ru");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user.getEmail());
        ChangePasswordDTO changePassword = new ChangePasswordDTO();
        changePassword.setOldPassword("123");
        changePassword.setNewPassword("321");
        CookieAssistant assistant= new CookieAssistant(mockMvc);
        Cookie[] allCookies = assistant.getAdminCookie("exist@e.ru");
        this.mockMvc.perform(post("/admin/change")
                .contentType(contentType)
                .content(gson.toJson(changePassword))
                .cookie(allCookies))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/admin/logout")
                .contentType(contentType)
                .cookie(allCookies))
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
