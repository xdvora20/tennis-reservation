package com.example.tennisreservation.dao;

import com.example.tennisreservation.entity.SurfaceType;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SurfaceTypeDao extends BaseDao<SurfaceType> {

    public SurfaceTypeDao() {
        super(SurfaceType.class);
    }

    public Optional<SurfaceType> findByName(String name) {
        return em.createQuery("SELECT s FROM SurfaceType s WHERE s.name = :name", SurfaceType.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }
}
