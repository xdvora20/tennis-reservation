package com.example.tennisreservation.dao;

import com.example.tennisreservation.entity.Reservation;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao extends BaseDao<Reservation> {

    public ReservationDao() {
        super(Reservation.class);
    }

    public List<Reservation> findByCourtNumber(Integer courtNumber) {
        return em.createQuery(
                        """
                        SELECT r FROM Reservation r
                        JOIN FETCH r.court c
                        JOIN FETCH r.customer
                        WHERE c.courtNumber = :number
                        ORDER BY r.createdAt, r.id
                        """,
                        Reservation.class)
                .setParameter("number", courtNumber)
                .getResultList();
    }

    public List<Reservation> findByCustomerPhone(String phoneNumber, boolean futureOnly) {
        String jpql =
                """
                SELECT r FROM Reservation r
                JOIN FETCH r.court
                JOIN FETCH r.customer cust
                WHERE cust.phoneNumber = :phone
                """
                        + (futureOnly ? "AND r.startTime > :now\n" : "")
                        + "ORDER BY r.createdAt, r.id";

        TypedQuery<Reservation> query =
                em.createQuery(jpql, Reservation.class).setParameter("phone", phoneNumber);
        if (futureOnly) {
            query.setParameter("now", LocalDateTime.now());
        }
        return query.getResultList();
    }

    public boolean existsOverlap(
            Long courtId, LocalDateTime start, LocalDateTime end, Long excludeReservationId) {
        String jpql =
                """
                SELECT COUNT(r) FROM Reservation r
                WHERE r.court.id = :courtId
                  AND r.startTime < :end
                  AND r.endTime > :start
                """
                        + (excludeReservationId != null ? "AND r.id <> :excludeId" : "");

        TypedQuery<Long> query =
                em.createQuery(jpql, Long.class)
                        .setParameter("courtId", courtId)
                        .setParameter("start", start)
                        .setParameter("end", end);
        if (excludeReservationId != null) {
            query.setParameter("excludeId", excludeReservationId);
        }
        return query.getSingleResult() > 0;
    }

    public boolean existsByCourtId(Long courtId) {
        return em.createQuery(
                                "SELECT COUNT(r) FROM Reservation r WHERE r.court.id = :courtId",
                                Long.class)
                        .setParameter("courtId", courtId)
                        .getSingleResult()
                > 0;
    }
}
