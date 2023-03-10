package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.USStates;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString(of = "name")
@Entity
@Table(name = "PROGRAM_AGGREGATOR")
public class ProgramAggregator extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;
    @Column(name = "MAILING_ADDRESS")
    private String mailingAddress;
    @Column(name = "MAILING_CITY")
    private String mailingCity;
    @Column(name = "MAILING_STATE")
    @Enumerated(EnumType.STRING)
    private USStates mailingState;
    @Column(name = "MAILING_ZIP_CODE")
    private String mailingZipCode;
    @Column(name = "PRIMARY_POC")
    private String primaryPOC;
    @Column(name = "PRIMARY_POC_PHONE")
    private String primaryPOCPhone;
    @Column(name = "EFFECTIVE_START_DATE")
    private Date effectiveStartDate;
    @Column(name = "EFFECTIVE_END_DATE")
    private Date effectiveEndDate;
    @Column(name = "WEBSITE")
    private String website;
    @Column(name = "STATUS")
    private String status;
}