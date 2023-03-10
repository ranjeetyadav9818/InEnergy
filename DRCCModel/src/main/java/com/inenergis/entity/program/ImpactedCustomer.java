package com.inenergis.entity.program;

import com.inenergis.entity.Event;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "IMPACTED_CUSTOMER")
@NoArgsConstructor
@AllArgsConstructor
public class ImpactedCustomer extends IdentifiableEntity {
    protected static final Logger log = LoggerFactory.getLogger(ImpactedCustomer.class);

    @ManyToOne
    @JoinColumn(name = "LOCATION_SUBMISSION_STATUS_ID")
    private LocationSubmissionStatus locationSubmissionStatus;
    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;
    @Column(name = "DRMS_ID")
    private String drmsId;
    @Column(name = "DRMS_PROGRAM_ID")
    private String drmsProgramId;
}