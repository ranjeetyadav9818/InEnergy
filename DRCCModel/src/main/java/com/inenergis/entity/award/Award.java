package com.inenergis.entity.award;


import com.inenergis.entity.Event;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.EventStatus;
import com.inenergis.entity.genericEnum.RetailDispatchScheduleType;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Getter
@Setter
@Entity
@Table(name = "AWARD")
public class Award extends IdentifiableEntity {

    @Column(name = "TRADE_DATE")
    private Date tradeDate;
    @Column(name = "START_TIME")
    private Date startTime;
    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "RETAIL_TYPE")
    @Enumerated(EnumType.STRING)
    private RetailDispatchScheduleType type;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID", insertable = false, updatable = false)
    private IsoResource resource;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID", insertable = false, updatable = false)
    private Event event;

    @OneToMany(mappedBy = "award", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Instruction> instructions;

    @OneToMany(mappedBy = "award", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Trajectory> trajectories;

    @OneToMany(mappedBy = "award", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<AwardException> exceptions;

    @Transient
    public boolean hasErrorEvents() {
        if (event != null) {
            return EventStatus.ERROR.equals(event.getStatus());
        }
        return false;
    }

    @Transient
    public long calculateDispatchTotalHours(){
        if(startTime == null || endTime == null){
            return 0;
        }
        return startTime.toInstant().until(endTime.toInstant(), ChronoUnit.HOURS);
    }

    public void addInstruction(Instruction instruction){
        if(getInstructions()==null){
            setInstructions(new ArrayList<>());
        }
        getInstructions().add(instruction);
    }

    public void addTrajectory(Trajectory trajectory){
        if(getTrajectories()==null){
            setTrajectories(new ArrayList<>());
        }
        getTrajectories().add(trajectory);
    }

    public void addAwardException(AwardException exception){
        if(getExceptions()==null){
            setExceptions(new ArrayList<>());
        }
        getExceptions().add(exception);
    }
}
