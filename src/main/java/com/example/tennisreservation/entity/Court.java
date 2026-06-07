package com.example.tennisreservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "court")
public class Court extends BaseEntity {

    @Column(name = "court_number", nullable = false, unique = true)
    private Integer courtNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "surface_type_id", nullable = false)
    private SurfaceType surfaceType;
}
