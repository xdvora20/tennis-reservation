package com.example.tennisreservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "surface_type")
public class SurfaceType extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "price_per_minute", nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerMinute;
}
