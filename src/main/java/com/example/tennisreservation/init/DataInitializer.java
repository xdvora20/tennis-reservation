package com.example.tennisreservation.init;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Role;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.service.UserService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "app.init-data", havingValue = "true")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SurfaceTypeDao surfaceTypeDao;
    private final CourtDao courtDao;
    private final UserService userService;

    @Override
    @Transactional
    public void run(String... args) {
        if (surfaceTypeDao.findAll().isEmpty()) {
            SurfaceType clay = surfaceTypeDao.save(surfaceType("Clay", "5.00"));
            SurfaceType grass = surfaceTypeDao.save(surfaceType("Grass", "7.00"));
            courtDao.save(court(1, clay));
            courtDao.save(court(2, clay));
            courtDao.save(court(3, grass));
            courtDao.save(court(4, grass));
        }
        if (userService.findByUsername("admin").isEmpty()) {
            userService.create("admin", "admin123", Role.ADMIN);
        }
        if (userService.findByUsername("user").isEmpty()) {
            userService.create("user", "user123", Role.USER);
        }
    }

    private SurfaceType surfaceType(String name, String pricePerMinute) {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName(name);
        surfaceType.setPricePerMinute(new BigDecimal(pricePerMinute));
        return surfaceType;
    }

    private Court court(int courtNumber, SurfaceType surfaceType) {
        Court court = new Court();
        court.setCourtNumber(courtNumber);
        court.setSurfaceType(surfaceType);
        return court;
    }
}
