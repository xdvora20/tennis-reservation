package com.example.tennisreservation.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(
        properties = {
            "app.init-data=true",
            // Isolated in-memory DB: the initializer's CommandLineRunner commits seed data at
            // startup (outside any test transaction), so it must not share the DB other ITs use.
            "spring.datasource.url=jdbc:h2:mem:tennisdb-init;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
        })
@ActiveProfiles("test")
@Transactional
class DataInitializerIT {

    @Autowired private SurfaceTypeDao surfaceTypeDao;
    @Autowired private CourtDao courtDao;
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void startup_initDataEnabled_seedsTwoSurfaceTypesAndFourCourts() {
        assertThat(surfaceTypeDao.findAll()).hasSize(2);
        assertThat(courtDao.findAll()).hasSize(4);
    }

    @Test
    void startup_initDataEnabled_seedsDefaultUsers() {
        var admin = userService.findByUsername("admin").orElseThrow();
        assertThat(admin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(passwordEncoder.matches("admin123", admin.getPassword())).isTrue();
        assertThat(userService.findByUsername("user")).isPresent();
    }
}
