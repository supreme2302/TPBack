package com.tpark.back.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import com.opentable.db.postgres.embedded.PgBinaryResolver;
import lombok.SneakyThrows;
import org.eclipse.jetty.util.IO;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.String.format;

@Configuration
@ComponentScan(basePackages = {"com.tpark.back"})
public class EmbeddedPostgresConfiguration {

    class ClasspathBinaryResolver implements PgBinaryResolver {
        public InputStream getPgBinary(String system, String machineHardware) throws IOException {
            ClassPathResource resource = new ClassPathResource(format("postgresql-%s-%s.txz", system, machineHardware));
            return resource.getInputStream();
        }
    }

    @SneakyThrows(IOException.class)
    @Bean
    public DataSource dataSource() {
        return EmbeddedPostgres.builder()
                .setPgBinaryResolver(new ClasspathBinaryResolver())
                .start()
                .getTemplateDatabase();
    }

    @Bean
    public PlatformTransactionManager dbTransactionManager() {
        DataSourceTransactionManager transactionManager
                = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }
}
