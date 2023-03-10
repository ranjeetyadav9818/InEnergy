package com.inenergis.entity;

import com.inenergis.entity.marketIntegration.Iso;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@ToString(of = "name")
@EqualsAndHashCode(of = "name", callSuper = false)
@Entity
@Table(name = "SUBLAP")
public class SubLap extends IdentifiableEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @OneToMany(mappedBy = "sublap")
    private List<SubLapPolygonCoordinates> coordinates;

    @ManyToOne
    @JoinColumn(name = "ISO_ID")
    private Iso iso;
}