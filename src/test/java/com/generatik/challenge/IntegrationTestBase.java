package com.generatik.challenge;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public abstract class IntegrationTestBase {

  static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:15")
          .withDatabaseName("booking-system-backend")
          .withUsername("postgres")
          .withPassword("password");

  static {
    POSTGRES.start();
  }

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);

    // IMPORTANT for tests: let Flyway run into the container DB
    registry.add("spring.flyway.enabled", () -> true);

    // Optional: avoid "validate" problems if schema isn't there yet
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
  }
}