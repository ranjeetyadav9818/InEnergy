package com.inenergis.entity.award;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.locationRegistration.IsoResource;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "AWARD_INSTRUCTION")
@EqualsAndHashCode(of = "instructionUID")
public class Instruction extends IdentifiableEntity {

    @Column(name = "INSTRUCTION_UID")
    private String instructionUID;

    @ManyToOne
    @JoinColumn(name = "AWARD_ID")
    private Award award;

    @Column(name = "STATUS_CODE")
    private String statusCode;
    @Column(name = "RETAIL_DISPATCH_SCH_TYPE")
    private String retailDispatchSchType;
    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID")
    IsoResource isoResource;

    @OneToMany(mappedBy = "instruction", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<InstructionDetail> instructionDetails;

    @Column(name = "CONFIGURATION_ID")
    private String configurationId;
    @Column(name = "START_TIME")
    private Date startTime;
    @Column(name = "END_TIME")
    private Date endTime;
    @Column(name = "DOT")
    private String DOT;
    @Column(name = "TYPE")
    private Integer Type; //Enum InstructionType
    @Column(name = "AGC_FLAG")
    private String agcFlag;
    @Column(name = "RMR_FLAG")
    private String rmrFlag;
    @Column(name = "VALIDATED")
    private Date validated;
    @Column(name = "VALIDATED_BY")
    private String validatedBy;
    @Column(name = "API_VALIDATED")
    private Date apiValidated;
    @Column(name = "API_VALIDATED_BY")
    private String apiValidatedBy;
    @Column(name = "REVISION_NUMBER")
    private Integer revisionNumber;
    @Column(name = "BID_DELAY")
    private Integer bidDelay;
    @Column(name = "ISO_START_TIME")
    private Date isoStartTime;
    @Column(name = "DISPUTE_STATUS")  //TODO Not sure if it should be placed here or at the exception
    private String disputeStatus;
    @Column(name = "ISO_ID")
    private String isoId;
    @Column(name = "ISO_RESOURCE_ID")
    private String isoResourceId; //name provided by CAISO. TODO if matches isoResource.NAME remove
    @Column(name = "XML_SOURCE", length = 65535,columnDefinition="Text")
    String xmlSource;
    @Column(name = "BATCH_SENT_TIME")
    private Date batchSentTime;
    @Column(name = "BATCH_RECEIVED_TIME")
    private Date batchReceivedTime;
}
