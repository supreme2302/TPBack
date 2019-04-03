package com.tpark.back.dao;

import com.tpark.back.config.EmbeddedPostgresConfiguration;
import com.tpark.back.dao.Impl.SchoolIDDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {EmbeddedPostgresConfiguration.class, MockMvcAutoConfiguration.class})
@TestPropertySource("/application-test.properties")
public class SchoolIdDAOTest {

    @Autowired
    private SchoolIDDAO schoolIDDAO;

    @Test
    public void getSchoolIdTest() throws Exception {
        Integer id = schoolIDDAO.getSchoolId("exist@e.ru");
        assertEquals(1, id.intValue());
    }
}
