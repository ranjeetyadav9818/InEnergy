package com.inenergis.entity.device;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DEVICE_LINE")
@Getter
@Setter
public class DeviceLine extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "START_LATITUDE")
    private BigDecimal startLatitude;

    @Column(name = "START_LONGITUDE")
    private BigDecimal startLongitude;

    @Column(name = "END_LATITUDE")
    private BigDecimal endLatitude;

    @Column(name = "END_LONGITUDE")
    private BigDecimal endLongitude;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "DEVICE_LINE_DEVICE", joinColumns = {@JoinColumn(name = "DEVICE_LINE_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "ASSET_DEVICE_ID", nullable = false)})
    private List<AssetDevice> devices;

}