package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.EventDurationOption;
import com.inenergis.entity.genericEnum.MinutesOrHoursOrDays;
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
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PROGRAM_CUSTOMER_NOTIFICATION")
public class ProgramCustomerNotification extends IdentifiableEntity {

    @Column(name = "NOTIFICATION_LEAD_TIME")
    private Short notificationLeadTime;

    @Column(name = "NOTIFICATION_LEAD_TIME_UNITS")
    @Enumerated(EnumType.STRING)
    private MinutesOrHoursOrDays notificationLeadTimeUnits;

    @Column(name = "NOTIFICATION_OPTION")
    @Enumerated(EnumType.STRING)
    private EventDurationOption eventDurationOption;

    // Seconds
    @Column(name = "LAST_NOTIFICATION_DELIVERY_BY")
    private Integer lastNotificationDeliveredBy;

    @OneToMany(mappedBy = "programCustomerNotification", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationDuplicationSource> notificationDuplicationSources;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Override
    public String toString() {
        return notificationLeadTime + " " + notificationLeadTimeUnits.getName();
    }
}