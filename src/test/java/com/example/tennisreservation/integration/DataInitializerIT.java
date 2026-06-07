package com.example.tennisreservation.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.dao.SurfaceTypeDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "app.init-data=true")
@ActiveProfiles("test")
@Transactional
class DataInitializerIT {

    @Autowired private SurfaceTypeDao surfaceTypeDao;
    @Autowired private CourtDao courtDao;

    @Test
    void seedsTwoSurfaceTypesAndFourCourtsOnStartup() {
        assertThat(surfaceTypeDao.findAll()).hasSize(2);
        assertThat(courtDao.findAll()).hasSize(4);
    }
}
