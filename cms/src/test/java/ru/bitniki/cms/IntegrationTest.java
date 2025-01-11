package ru.bitniki.cms;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
public class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer("postgres:15")
                .withDatabaseName("vpncon")
                .withUsername("postgres")
                .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    private static void runMigrations(JdbcDatabaseContainer<?> c) {
        var changelogPath = new File(".").toPath().toAbsolutePath().getParent().getParent().resolve("migrations");

        try {
            java.sql.Connection connection = c.createConnection("");

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            FileSystemResourceAccessor resourceAccessor = new FileSystemResourceAccessor(changelogPath.toFile());
            Liquibase liquibase = new Liquibase("master.xml", resourceAccessor, database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            throw new RuntimeException("Failed to run migrations", e);
        }

    }



    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        // jdbc
        registry.add("spring.datasource.url", POSTGRES:: getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        // r2dbc
        registry.add(
            "spring.r2dbc.url",
                () -> POSTGRES.getJdbcUrl().replace("jdbc", "r2dbc")
        );
        registry.add("spring.r2dbc.username", POSTGRES::getUsername);
        registry.add("spring.r2dbc.password", POSTGRES::getPassword);
    }
}
