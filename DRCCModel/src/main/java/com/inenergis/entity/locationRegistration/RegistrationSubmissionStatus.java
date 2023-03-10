package com.inenergis.entity.locationRegistration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "REGISTRATION_STATUS")
@ToString(of = "isoName")
public class RegistrationSubmissionStatus extends IdentifiableEntity {

    @Column(name = "ACTIVE_START_DATE")
    private Date activeStartDate;

    @Column(name = "ACTIVE_END_DATE")
    private Date activeEndDate;


    @Column(name = "ENOUGH_DAYS_METER")
    private String enoughDaysMeter;

    @Column(name = "REGISTRATION_STATUS")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    @Column(name = "ISO_REGISTRATION_ID")
    private String isoRegistrationId;

    @Column(name = "ISO_NAME")
    private String isoName;

    @Column(name = "ISO_BATCH_ID")
    private String isoBatchId;

    @Column(name = "INCONSISTENCY_SOLVED")
    private boolean inconsistencySolved;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID")
    IsoResource isoResource;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "LOCATION_REGISTRATION_LINKS", joinColumns = {@JoinColumn(name = "REGISTRATION_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "LOCATION_ID", nullable = false)})
    List<LocationSubmissionStatus> locations;


    @OneToMany(mappedBy = "registrationSubmissionStatus", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistrationSubmissionException> exceptions;

    public boolean isActive() {
        if (getActiveStartDate() == null) {
            return false;
        }
        if (registrationStatus.equals(RegistrationStatus.REGISTERED) || registrationStatus.equals(RegistrationStatus.WAITING_FOR_SQMD) || registrationStatus.equals(RegistrationStatus.FINISHED)) {
            Date now = Date.from(ZonedDateTime.now(ZoneId.systemDefault()).toInstant());
            if (now.after(getActiveStartDate()) && now.before(getActiveEndDate())) {
                return true;
            }
        }
        return false;
    }

    public boolean isActiveInISO() {
        return registrationStatus.equals(RegistrationStatus.REGISTERED) || registrationStatus.equals(RegistrationStatus.WAITING_FOR_SQMD);
    }

    public enum RegistrationStatus {
        PENDING("Submitted to CAISO & awaiting ID back"),
        WAITING_FOR_SQMD("Submitted to SQMD & awaiting request for registration details"),
        REGISTERED("Registered"),
        PROCESSING_ERROR("Processing error"),
        FINISHED_PENDING_ISO("End dated (Iso confirmation pending)"),
        FINISHED("End dated");

        private final String text;

        RegistrationStatus(final String text) {
            this.text = text;
        }

        public String toString() {
            return this.name();
        }

        public String getText() {
            return text;
        }

        public static RegistrationStatus valueFromText(String s) {
            for (RegistrationStatus registrationStatus : values()) {
                if (registrationStatus.getText().equalsIgnoreCase(s)) {
                    return registrationStatus;
                }
            }
            return null;
        }
    }
}