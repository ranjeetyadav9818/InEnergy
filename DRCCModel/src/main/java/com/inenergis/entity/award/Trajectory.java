package com.inenergis.entity.award;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.locationRegistration.IsoResource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "AWARD_TRAJECTORY")
@EqualsAndHashCode(of = "trajectoryUID")
public class Trajectory extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "AWARD_ID")
    private Award award;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID")
    private IsoResource resource;

    @Column(name = "DOP")
    private Long dop;

    @Column(name = "TARGET_TIME")
    private Date targetTime;

    @Column(name = "SEQUENCE_NUMBER")
    private String sequenceNumber;
    @Column(name = "CONFIGURATION_ID")
    private String configurationId;
    @Column(name = "DOP_UID")
    private String dopUID;
    @Column(name = "XML_SOURCE", length = 65535,columnDefinition="Text")
    String xmlSource;
    @Column(name = "BATCH_SENT_TIME")
    private Date batchSentTime;
    @Column(name = "BATCH_RECEIVED_TIME")
    private Date batchReceivedTime;
}
