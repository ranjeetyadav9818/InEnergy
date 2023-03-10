package com.inenergis.entity;

import com.inenergis.entity.award.Award;
import com.inenergis.entity.genericEnum.DispatchCancelReason;
import com.inenergis.entity.genericEnum.DispatchLevel;
import com.inenergis.entity.genericEnum.DispatchReason;
import com.inenergis.entity.genericEnum.EventStatus;
import com.inenergis.entity.genericEnum.EventType;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.ImpactedResource;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramOption;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "EVENT")
public class Event extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    Program program;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_OPTION_ID")
    private ProgramOption programOption;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Award> awards;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "SCHEDULED_DATE")
    private Date scheduledDate = new Date();

    @Column(name = "ERROR_REASON")
    private String errorReason;

    @Column(name = "DISPATCH_LEVEL")
    @Enumerated(value = EnumType.STRING)
    private DispatchLevel dispatchLevel;

    @Column(name = "SA_COUNT")
    private Integer saCount;

    @Column(name = "DISPATCH_REASON")
    @Enumerated(value = EnumType.STRING)
    private DispatchReason dispatchReason;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "CANCEL_REASON")
    @Enumerated(EnumType.STRING)
    private DispatchCancelReason cancelReason;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventDispatchLevel> eventDispatchLevels;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImpactedCustomer> impactedCustomers;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImpactedResource> impactedResources;

    @Column(name = "EXTERNAL_EVENT_ID")
    private String externalEventId;

    @Column(name = "DRMS_PROGRAM_ID")
    private String programId; // DRMS ID

    @Column(name = "CREATED_ON")
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "EVENT_NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "EVENT_NOTIFICATION_ID")
    private PdpSrEvent eventNotification;


    @Transient
    private List<String> servicePointIds = new ArrayList<>();

    public void setImpactedCustomers(List<ImpactedCustomer> impactedCustomers) {
        this.impactedCustomers = impactedCustomers;
        saCount = impactedCustomers.size();

        this.impactedResources = impactedCustomers.stream()
                .map(c -> c.getLocationSubmissionStatus().getActiveResource())
                .filter(Objects::nonNull)
                .distinct()
                .map(isoResource -> new ImpactedResource(isoResource, this))
                .collect(Collectors.toList());
    }

    public Duration getDuration() {
        if (startDate == null || endDate == null) {
            return Duration.ZERO;
        }

        return Duration.between(startDate.toInstant(), endDate.toInstant());
    }

    public boolean isStartDateOn(LocalDate compareDate) {
        LocalDate localStartDate = startDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDate();
        return localStartDate.isEqual(compareDate);
    }

    public boolean isStartDateOn(int year, Month month) {
        LocalDateTime localStartDate = LocalDateTime.from(startDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
        return localStartDate.getYear() == year && localStartDate.getMonth().equals(month);
    }

    public boolean isStartDateOn(int year) {
        LocalDate localStartDate = LocalDate.from(startDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
        return localStartDate.getYear() == year;
    }

    public boolean isStartDateBefore(LocalDate compareDate) {
        LocalDate localStartDate = LocalDate.from(startDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
        return localStartDate.isBefore(compareDate);
    }

    public boolean containsLocation(LocationSubmissionStatus location) {
        for (ImpactedCustomer impactedCustomer : this.getImpactedCustomers()) {
            if (impactedCustomer.getLocationSubmissionStatus().equals(location)) {
                return true;
            }
        }
        return false;
    }

    public boolean isStarted() {
        return !startDate.toInstant().isAfter(new Date().toInstant().plusSeconds(60));
    }

    public boolean isCompleted() {
        return endDate.toInstant().isBefore(new Date().toInstant());
    }

    public void setStatus(EventStatus status) {
        this.status = status;

        if (status.equals(EventStatus.CANCELLED) && isStarted()) {
            endDate = new Date();
            this.status = EventStatus.TERMINATED;
        }
    }

    public EventType getEventType() {

        final LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        final LocalDateTime startDate = LocalDateTime.from(getStartDate().toInstant().atZone(ZoneId.systemDefault()));
        final LocalDateTime endDate = LocalDateTime.from(getEndDate().toInstant().atZone(ZoneId.systemDefault()));
        if (now.isBefore(startDate)) {
            return EventType.SCHEDULED;
        } else if (now.isAfter(endDate)){
            return EventType.COMPLETED;
        } else {
            return EventType.IN_PROGRESS;
        }
    }
}