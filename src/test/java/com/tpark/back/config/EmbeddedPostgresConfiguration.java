package com.tpark.back.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import com.opentable.db.postgres.embedded.PgBinaryResolver;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.flywaydb.core.Flyway;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

@Configuration
@ComponentScan(basePackages = {"com.tpark.back"})
public class EmbeddedPostgresConfiguration {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedPostgresConfiguration.class);

    class ClasspathBinaryResolver implements PgBinaryResolver {
        public InputStream getPgBinary(String system, String machineHardware) throws IOException {
            ClassPathResource resource = new ClassPathResource(format("postgresql-%s-%s.txz", system, machineHardware));
            return resource.getInputStream();
        }
    }

    @SneakyThrows(IOException.class)
    @Bean("embeddedDataSource")
    public DataSource dataSource() {
        return EmbeddedPostgres.builder()
                .setPgBinaryResolver(new ClasspathBinaryResolver())
                .start()
                .getTemplateDatabase();
    }

    @Bean
    public PlatformTransactionManager tx(@Qualifier("embeddedDataSource") DataSource dataSource) {
        DataSourceTransactionManager transactionManager
                = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public Flyway flyway(@Qualifier("embeddedDataSource") DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        logger.info("flyway working---------------------------------------------------");
        flyway.migrate();
        return flyway;
    }
}
