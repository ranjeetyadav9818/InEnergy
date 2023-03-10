package com.inenergis.entity;

import com.inenergis.entity.program.SublapProgramMapping;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@ToString(of = "name")
@EqualsAndHashCode(of = "name", callSuper = false)
@Entity
@Table(name = "SUBLAP_POLYGON_COORDINATES")
public class SubLapPolygonCoordinates extends IdentifiableEntity {
    @Column(name = "COORDINATE_ORDER")
    private int order;

   @Column(name = "LATITUDE")
    private BigDecimal latitude;

    @Column(name = "LONGITUDE")
    private BigDecimal longitude;

    @ManyToOne
    @JoinColumn(name = "SUBLAP_ID")
    private SubLap sublap;
}