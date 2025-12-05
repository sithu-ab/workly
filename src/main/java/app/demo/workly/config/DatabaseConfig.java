package app.demo.workly.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPwd;

    @Bean
    CommandLineRunner initDatabase(JdbcTemplate jdbcTemplate) {
        return args -> {
            // This flyway config is not needed because we will handle it by command using flyway plugin
            Flyway flyway = Flyway.configure()
                    .dataSource(dbUrl, dbUser, dbPwd)
                    .load();
            // flyway.migrate();

            System.out.println("Database initialized successfully!");
        };
    }
}
