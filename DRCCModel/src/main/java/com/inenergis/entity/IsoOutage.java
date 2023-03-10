package com.inenergis.entity;

import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.program.ImpactedResource;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ISO_OUTAGE")
public class IsoOutage extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID")
    IsoResource isoResource;

    @ManyToOne
    @JoinColumn(name = "IMPACTED_RESOURCE_ID")
    ImpactedResource impactedResource;

    @ManyToOne
    @JoinColumn(name = "REGISTRATION_ID")
    RegistrationSubmissionStatus registration;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "OUTAGE_ID")
    private String outageId;

    public IsoOutage() {
        // JPA requires to have a no-arg constructor
    }

    public IsoOutage(ImpactedResource impactedResource) {
        this.date = impactedResource.getEvent().getStartDate();
        this.impactedResource = impactedResource;
        this.isoResource = impactedResource.getIsoResource();
        this.registration = this.isoResource.getActiveRegistration();
    }

    public boolean hasOutage() {
        return StringUtils.isNotEmpty(outageId);
    }
}