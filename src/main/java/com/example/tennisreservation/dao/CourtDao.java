package com.example.tennisreservation.dao;

import com.example.tennisreservation.entity.Court;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CourtDao extends BaseDao<Court> {

    public CourtDao() {
        super(Court.class);
    }

    public Optional<Court> findByCourtNumber(Integer courtNumber) {
        return em.createQuery("SELECT c FROM Court c WHERE c.courtNumber = :number", Court.class)
                .setParameter("number", courtNumber)
                .getResultList()
                .stream()
                .findFirst();
    }
}
