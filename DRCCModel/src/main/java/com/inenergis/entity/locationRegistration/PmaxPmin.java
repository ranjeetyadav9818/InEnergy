package com.inenergis.entity.locationRegistration;

import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "PMAX_PMIN")
public class PmaxPmin extends IdentifiableEntity {

    @Column(name = "ESTIMATED_CAPACITY")
    private Integer estimated_capacity;

    @Column(name = "PMIN")
    private Integer pmin;

    @Column(name = "PMAX")
    private Integer pmax;

    @Column(name = "EFFECTIVE_START_DATE")
    private Date effectiveStartDate;

    @Column(name = "EFFECTIVE_END_DATE")
    private Date effectiveEndDate;

    @Column(name = "LAST_UPDATE_DATE")
    Date lastUpdateDate;

    @Column(name = "LAST_UPDATE_BY")
    String lastUpdateBy;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID")
    IsoResource isoResource;
}