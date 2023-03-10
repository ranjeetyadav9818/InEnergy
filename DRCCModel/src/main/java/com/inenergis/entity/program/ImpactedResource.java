package com.inenergis.entity.program;

import com.inenergis.entity.Event;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.IsoOutage;
import com.inenergis.entity.locationRegistration.IsoResource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "isoResource", callSuper = false)
@Table(name = "IMPACTED_RESOURCE")
public class ImpactedResource extends IdentifiableEntity {
    protected static final Logger log = LoggerFactory.getLogger(ImpactedResource.class);

    @ManyToOne
    @JoinColumn(name = "ISO_RESOURCE_ID")
    private IsoResource isoResource;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    @Column(name = "TOTAL_IMPACTED_LOCATIONS")
    private long totalImpactedLocations;

    @Column(name = "IMPACTED_BY")
    private String impactedBy = "Awarded";

    @Transient
    private IsoOutage isoOutage;

    public ImpactedResource() {
        // JPA requires to have a no-arg constructor
    }

    public ImpactedResource(IsoResource isoResource, Event event) {
        this.isoResource = isoResource;
        this.event = event;
        totalImpactedLocations = event.getImpactedCustomers().stream()
                .filter(customer -> isoResource.equals(customer.getLocationSubmissionStatus().getActiveResource()))
                .count();
    }
}