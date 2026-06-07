package com.example.tennisreservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(SurfaceTypeDao.class)
@ActiveProfiles("test")
class SurfaceTypeDaoTest {

    @Autowired
    private SurfaceTypeDao dao;
    @Autowired
    private TestEntityManager tem;

    @Test
    void save_newEntity_persistsAndAssignsId() {
        SurfaceType saved = dao.save(SurfaceTypeTestDataFactory.surfaceType());

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void save_existingEntity_updatesIt() {
        SurfaceType saved = dao.save(SurfaceTypeTestDataFactory.surfaceType("Clay", "5.00"));
        flushAndClear();

        saved.setPricePerMinute(new BigDecimal("9.50"));
        dao.save(saved);
        flushAndClear();

        SurfaceType reloaded = dao.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Clay");
        assertThat(reloaded.getPricePerMinute()).isEqualByComparingTo("9.50");
    }

    @Test
    void findById_existingEntity_returnsIt() {
        Long id = dao.save(SurfaceTypeTestDataFactory.surfaceType("Clay", "5.00")).getId();
        flushAndClear();

        SurfaceType loaded = dao.findById(id).orElseThrow();
        assertThat(loaded.getName()).isEqualTo("Clay");
        assertThat(loaded.getPricePerMinute()).isEqualByComparingTo("5.00");
    }

    @Test
    void findById_missingEntity_returnsEmpty() {
        assertThat(dao.findById(999L)).isEmpty();
    }

    @Test
    void findAll_withSavedEntity_returnsIt() {
        Long id = dao.save(SurfaceTypeTestDataFactory.surfaceType()).getId();
        flushAndClear();

        assertThat(dao.findAll()).extracting(SurfaceType::getId).containsExactly(id);
    }

    @Test
    void deleteById_existingEntity_softDeletesAndHidesFromQueries() {
        Long id = dao.save(SurfaceTypeTestDataFactory.surfaceType()).getId();
        flushAndClear();

        assertThat(dao.deleteById(id)).isTrue();
        flushAndClear();

        assertThat(dao.findById(id)).isEmpty();
        assertThat(dao.findAll()).isEmpty();
    }

    @Test
    void deleteById_missingEntity_returnsFalse() {
        assertThat(dao.deleteById(999L)).isFalse();
    }

    @Test
    void delete_detachedEntity_softDeletesIt() {
        SurfaceType saved = dao.save(SurfaceTypeTestDataFactory.surfaceType());
        flushAndClear();

        dao.delete(saved);
        flushAndClear();

        assertThat(dao.findById(saved.getId())).isEmpty();
    }

    @Test
    void findByName_existingName_returnsMatch() {
        dao.save(SurfaceTypeTestDataFactory.surfaceType("Clay", "5.00"));
        flushAndClear();

        assertThat(dao.findByName("Clay")).get().extracting(SurfaceType::getName).isEqualTo("Clay");
    }

    @Test
    void findByName_missingName_returnsEmpty() {
        assertThat(dao.findByName("Concrete")).isEmpty();
    }

    private void flushAndClear() {
        tem.flush();
        tem.clear();
    }
}
