package com.example.tennisreservation.init;

import com.example.tennisreservation.dao.CourtDao;
import com.example.tennisreservation.dao.SurfaceTypeDao;
import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.SurfaceType;
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

    @Override
    @Transactional
    public void run(String... args) {
        if (!surfaceTypeDao.findAll().isEmpty()) {
            return;
        }
        SurfaceType clay = surfaceTypeDao.save(surfaceType("Clay", "5.00"));
        SurfaceType grass = surfaceTypeDao.save(surfaceType("Grass", "7.00"));
        courtDao.save(court(1, clay));
        courtDao.save(court(2, clay));
        courtDao.save(court(3, grass));
        courtDao.save(court(4, grass));
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
