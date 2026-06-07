package com.example.tennisreservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.utils.CourtTestDataFactory;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(CourtDao.class)
@ActiveProfiles("test")
class CourtDaoTest {

    @Autowired
    private CourtDao dao;
    @Autowired
    private TestEntityManager tem;

    private SurfaceType surface;

    @BeforeEach
    void persistSurface() {
        surface = tem.persist(SurfaceTypeTestDataFactory.surfaceType());
    }

    @Test
    void save_newEntity_persistsAndAssignsId() {
        Court saved = dao.save(CourtTestDataFactory.court(1, surface));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void save_existingEntity_updatesIt() {
        Court saved = dao.save(CourtTestDataFactory.court(1, surface));
        flushAndClear();

        saved.setCourtNumber(2);
        dao.save(saved);
        flushAndClear();

        assertThat(dao.findById(saved.getId()).orElseThrow().getCourtNumber()).isEqualTo(2);
    }

    @Test
    void findById_existingEntity_returnsIt() {
        Long id = dao.save(CourtTestDataFactory.court(1, surface)).getId();
        flushAndClear();

        Court loaded = dao.findById(id).orElseThrow();
        assertThat(loaded.getCourtNumber()).isEqualTo(1);
        assertThat(loaded.getSurfaceType()).isNotNull();
        assertThat(loaded.getSurfaceType().getName()).isEqualTo("Clay");
    }

    @Test
    void findById_missingEntity_returnsEmpty() {
        assertThat(dao.findById(999L)).isEmpty();
    }

    @Test
    void findAll_withSavedEntity_returnsIt() {
        Long id = dao.save(CourtTestDataFactory.court(1, surface)).getId();
        flushAndClear();

        assertThat(dao.findAll()).extracting(Court::getId).containsExactly(id);
    }

    @Test
    void deleteById_existingEntity_softDeletesAndHidesFromQueries() {
        Long id = dao.save(CourtTestDataFactory.court(1, surface)).getId();
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
        Court saved = dao.save(CourtTestDataFactory.court(1, surface));
        flushAndClear();

        dao.delete(saved);
        flushAndClear();

        assertThat(dao.findById(saved.getId())).isEmpty();
    }

    @Test
    void findByCourtNumber_existingNumber_returnsMatch() {
        dao.save(CourtTestDataFactory.court(1, surface));
        flushAndClear();

        assertThat(dao.findByCourtNumber(1))
                .get()
                .extracting(Court::getCourtNumber)
                .isEqualTo(1);
    }

    @Test
    void findByCourtNumber_missingNumber_returnsEmpty() {
        assertThat(dao.findByCourtNumber(99)).isEmpty();
    }

    private void flushAndClear() {
        tem.flush();
        tem.clear();
    }
}
