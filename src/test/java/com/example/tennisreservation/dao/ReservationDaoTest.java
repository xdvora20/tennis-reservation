package com.example.tennisreservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.tennisreservation.entity.Court;
import com.example.tennisreservation.entity.Customer;
import com.example.tennisreservation.entity.GameType;
import com.example.tennisreservation.entity.Reservation;
import com.example.tennisreservation.entity.SurfaceType;
import com.example.tennisreservation.utils.CourtTestDataFactory;
import com.example.tennisreservation.utils.CustomerTestDataFactory;
import com.example.tennisreservation.utils.ReservationTestDataFactory;
import com.example.tennisreservation.utils.SurfaceTypeTestDataFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(ReservationDao.class)
@ActiveProfiles("test")
class ReservationDaoTest {

    @Autowired
    private ReservationDao dao;
    @Autowired
    private TestEntityManager tem;

    private Court court1;
    private Court court2;
    private Customer alice;
    private Customer bob;

    @BeforeEach
    void persistPrerequisites() {
        SurfaceType clay = tem.persist(SurfaceTypeTestDataFactory.surfaceType());
        court1 = tem.persist(CourtTestDataFactory.court(1, clay));
        court2 = tem.persist(CourtTestDataFactory.court(2, clay));
        alice = tem.persist(CustomerTestDataFactory.customer("+420700111222", "Alice"));
        bob = tem.persist(CustomerTestDataFactory.customer("+420700333444", "Bob"));
    }

    @Test
    void save_newEntity_persistsAndAssignsId() {
        Reservation saved = save(court1, alice, at(10), at(11));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void save_existingEntity_updatesIt() {
        Reservation saved = save(court1, alice, at(10), at(11));
        flushAndClear();

        saved.setGameType(GameType.DOUBLES);
        saved.setTotalPrice(new BigDecimal("150.00"));
        dao.save(saved);
        flushAndClear();

        Reservation reloaded = dao.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getGameType()).isEqualTo(GameType.DOUBLES);
        assertThat(reloaded.getTotalPrice()).isEqualByComparingTo("150.00");
    }

    @Test
    void findById_existingEntity_returnsIt() {
        Long id = save(court1, alice, at(10), at(11)).getId();
        flushAndClear();

        Reservation loaded = dao.findById(id).orElseThrow();
        assertThat(loaded.getStartTime()).isEqualTo(at(10));
        assertThat(loaded.getEndTime()).isEqualTo(at(11));
        assertThat(loaded.getGameType()).isEqualTo(GameType.SINGLES);
        assertThat(loaded.getCourt().getCourtNumber()).isEqualTo(1);
        assertThat(loaded.getCustomer().getPhoneNumber()).isEqualTo(alice.getPhoneNumber());
    }

    @Test
    void findById_missingEntity_returnsEmpty() {
        assertThat(dao.findById(999L)).isEmpty();
    }

    @Test
    void findAll_withSavedEntity_returnsIt() {
        Long id = save(court1, alice, at(10), at(11)).getId();
        flushAndClear();

        assertThat(dao.findAll()).extracting(Reservation::getId).containsExactly(id);
    }

    @Test
    void deleteById_existingEntity_softDeletesAndHidesFromQueries() {
        Long id = save(court1, alice, at(10), at(11)).getId();
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
        Reservation saved = save(court1, alice, at(10), at(11));
        flushAndClear();

        dao.delete(saved);
        flushAndClear();

        assertThat(dao.findById(saved.getId())).isEmpty();
    }

    @Test
    void findByCourtNumber_multipleCourts_returnsOnlyThatCourtOrderedByCreatedAt()
            throws InterruptedException {
        Reservation first = save(court1, alice, at(10), at(11));
        Thread.sleep(10); // ensure a distinct @CreationTimestamp so ordering is deterministic
        Reservation second = save(court1, alice, at(12), at(13));
        save(court2, bob, at(10), at(11));
        flushAndClear();

        assertThat(dao.findByCourtNumber(1))
                .extracting(Reservation::getId)
                .containsExactly(first.getId(), second.getId());
    }

    @Test
    void findByCourtNumber_unknownCourt_returnsEmpty() {
        save(court1, alice, at(10), at(11));
        flushAndClear();

        assertThat(dao.findByCourtNumber(999)).isEmpty();
    }

    @Test
    void findByCustomerPhone_multipleReservations_returnsOrderedByCreatedAt()
            throws InterruptedException {
        Reservation first = save(court1, alice, at(10), at(11));
        Thread.sleep(10); // ensure a distinct @CreationTimestamp so ordering is deterministic
        Reservation second = save(court1, alice, at(12), at(13));
        flushAndClear();

        assertThat(dao.findByCustomerPhone(alice.getPhoneNumber(), false))
                .extracting(Reservation::getId)
                .containsExactly(first.getId(), second.getId());
    }

    @Test
    void findByCustomerPhone_notFutureOnly_returnsAll() {
        save(court1, alice, past(), past().plusHours(1));
        save(court1, alice, future(), future().plusHours(1));
        flushAndClear();

        assertThat(dao.findByCustomerPhone(alice.getPhoneNumber(), false)).hasSize(2);
    }

    @Test
    void findByCustomerPhone_futureOnly_returnsOnlyFuture() {
        save(court1, alice, past(), past().plusHours(1));
        Reservation upcoming = save(court1, alice, future(), future().plusHours(1));
        flushAndClear();

        assertThat(dao.findByCustomerPhone(alice.getPhoneNumber(), true))
                .extracting(Reservation::getId)
                .containsExactly(upcoming.getId());
    }

    @Test
    void findByCustomerPhone_unknownPhone_returnsEmpty() {
        save(court1, alice, at(10), at(11));
        flushAndClear();

        assertThat(dao.findByCustomerPhone("+420700000000", false)).isEmpty();
    }

    @Test
    void existsOverlap_overlappingInterval_returnsTrue() {
        save(court1, alice, at(10), at(11));
        tem.flush();

        assertThat(dao.existsOverlap(court1.getId(), at(10, 30), at(11, 30), null)).isTrue();
    }

    @Test
    void existsOverlap_adjacentIntervals_returnsFalse() {
        save(court1, alice, at(10), at(11));
        tem.flush();

        assertThat(dao.existsOverlap(court1.getId(), at(11), at(12), null)).isFalse();
        assertThat(dao.existsOverlap(court1.getId(), at(9), at(10), null)).isFalse();
    }

    @Test
    void existsOverlap_otherCourt_returnsFalse() {
        save(court1, alice, at(10), at(11));
        tem.flush();

        assertThat(dao.existsOverlap(court2.getId(), at(10), at(11), null)).isFalse();
    }

    @Test
    void existsOverlap_excludingGivenReservation_returnsFalse() {
        Reservation existing = save(court1, alice, at(10), at(11));
        tem.flush();

        assertThat(dao.existsOverlap(court1.getId(), at(10), at(11), existing.getId())).isFalse();
    }

    @Test
    void existsOverlap_softDeletedReservation_returnsFalse() {
        Reservation existing = save(court1, alice, at(10), at(11));
        tem.flush();
        dao.delete(existing);
        tem.flush();

        assertThat(dao.existsOverlap(court1.getId(), at(10), at(11), null)).isFalse();
    }

    private Reservation save(
            Court court, Customer customer, LocalDateTime start, LocalDateTime end) {
        return dao.save(
                ReservationTestDataFactory.reservation(
                        court, customer, start, end, GameType.SINGLES));
    }

    private void flushAndClear() {
        tem.flush();
        tem.clear();
    }

    private static LocalDateTime at(int hour) {
        return LocalDateTime.of(2030, 1, 1, hour, 0);
    }

    private static LocalDateTime at(int hour, int minute) {
        return LocalDateTime.of(2030, 1, 1, hour, minute);
    }

    private static LocalDateTime past() {
        return LocalDateTime.now().minusDays(1);
    }

    private static LocalDateTime future() {
        return LocalDateTime.now().plusDays(1);
    }
}
