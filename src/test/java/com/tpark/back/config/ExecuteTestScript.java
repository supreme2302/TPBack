package com.tpark.back.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ExecuteTestScript {

    private static final Logger log = LoggerFactory.getLogger(ExecuteTestScript.class);

    private final DataSource dataSource;
    private final ClassPathResource classPathResource;

    public ExecuteTestScript(DataSource dataSource, ClassPathResource classPathResource) {
        this.dataSource = dataSource;
        this.classPathResource = classPathResource;
    }

    @PostConstruct
    public void init() {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, classPathResource);
            log.info("test script executed");
        } catch (SQLException ex){
            log.info("test script failed. Exit");
            throw new RuntimeException(ex);
        }
    }
}
