package com.inenergis.entity.locationRegistration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramFirmServiceLevel;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.trove.MeterForecast;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.EnergyUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.inenergis.util.ConstantsProviderModel.WEEKDAYS;

@Entity
@Getter
@Setter
@Table(name = "LOCATION_SUBMISSION_STATUS")
@ToString
public class LocationSubmissionStatus extends IdentifiableEntity {

    //enum
    public enum LocationStatus {
        PENDING_APRPOVAL("Processing"),
        PENDING_TO_UNREGISTER("Unregistering in Market"),
        INACTIVE("Inactive"),
        INACTIVE_INFORMED_TO_ISO("Inactive but pending association"),
        EXCEPTIONS("Exceptions"),
        PENDING_REPROCESS("Reprocessing"),
        DUPLICATED("Duplicated"),
        DISPUTED("Disputed"),
        PENDING_REVIEW("Pending LSE/UDC Review"),
        ASSIGNED_TO_RESOURCE("Active/Assigned to Resource"),
        CANCELED("Canceled"),
        WITHDRAWN("Withdrawn"),
        RESUBMITTED("Resubmitted"),
        NON_REGISTRABLE("Non-Registrable"),
        NON_REGISTRABLE_CANCELED("Non-Registrable (Canceled)");

        private final String text;

        private LocationStatus(final String text) {
            this.text = text;
        }

        public String toString() {
            return text;
        }

        public String getText() {
            return text;
        }

        public static LocationStatus valueFromText(String s) {
            for (LocationStatus locationStatus : values()) {
                if (locationStatus.getText().equalsIgnoreCase(s)) {
                    return locationStatus;
                }
            }
            return null;
        }
    }

    //iso registration id corresponding to the field view Location ID

    ///* TODO CAISO SPECIFIC. Decouple to another class

    @Column(name = "SUBLAP")
    private String isoSublap;

    @Column(name = "LSE")
    private String isoLse;

    @Column(name = "ISO_SUBMISSION_DATE")
    private Date isoSubmissionDate;

    @Column(name = "ISO_RESOURCE_ID")
    private String isoResourceId;

    @Column(name = "ISO_BATCH_ID")
    private String isoBatchId;

    @Column(name = "ISO_NON_REG_REASON")
    private String isoNonRegistrableReason;

    @Column(name = "ISO_NON_REG_DATE")
    private Date isoNonRegistrableDate;

    @Column(name = "ISO_START_DATE")
    private Date isoStartDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PROGRAM_SA_ENROLLMENT_ID")
    private ProgramServiceAgreementEnrollment programServiceAgreementEnrollment;

    @ManyToOne
    @JoinColumn(name = "ISO_ID")
    private Iso iso;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "METER_DATA_RECHECK")
    private Boolean meterDataRecheck;

    @OneToMany(mappedBy = "locationSubmissionStatus", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocationSubmissionException> exceptions;

    @OneToMany(mappedBy = "locationSubmissionStatus", fetch = FetchType.LAZY)
    private List<ImpactedCustomer> eventAppearances;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "locations")
    private List<RegistrationSubmissionStatus> registrations;



    @Transient
    private String isoStatus;
    @Transient
    private String previousIsoId;

    @Transient
    private boolean isExhausted = false;

    @Transient
    private LocationChangelog changelog;

    @Transient
    public int daysOutStanding() {
        int days = 0;
        if (isoSubmissionDate != null) {
            LocalDate startDate = isoSubmissionDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDate();
            LocalDate endDate = LocalDate.now();

            while (startDate.isBefore(endDate)) {
                if (WEEKDAYS.contains(startDate.getDayOfWeek())) {
                    days++;
                }
                startDate = startDate.plusDays(1);
            }
        }

        return days;
    }

    public boolean canContinue() {
        if (exceptions != null) {
            LocationSubmissionException locationSubmissionException = exceptions.get(0);
            return locationSubmissionException != null && locationSubmissionException.isCanContinue();
        }
        return true;
    }

    @Transient
    public Program getProgram() {
        return programServiceAgreementEnrollment.getProgram();
    }

    @Transient
    public Pair<String, String> getLseAndSublapCombined() {
        return new ImmutablePair<>(isoLse, isoSublap);
    }

    @Transient
    public long getCalculatedCapacity() {
        Long capacity = getProgramServiceAgreementEnrollment().getAverageSummerOnPeakWatt();

        ProgramFirmServiceLevel fsl = getProgramServiceAgreementEnrollment().getCurrentFSL();
        if (fsl != null) {
            capacity -= EnergyUtil.convertToWatts(fsl.getValue().longValue(), ElectricalUnit.KW);
        }

        return Long.max(capacity, 0);
    }

    @Transient
    public BigDecimal getCalculatedCapacityAsKwDecimal() {
        BigDecimal divide = BigDecimal.valueOf(getCalculatedCapacity()).divide(BigDecimal.valueOf(ConstantsProviderModel.KW_PRECISION));
        return divide;
    }

    @Transient
    public long getCalculatedCapacity(HourEnd hourEnd) {
        return getCalculatedCapacity(new Date(), hourEnd);
    }

    @Transient
    public long getCalculatedCapacity(Date date, HourEnd hourEnd) {
        long totalForecastCalculation = 0;
        List<ProgramFirmServiceLevel> fsls = getProgramServiceAgreementEnrollment().getFsls();
        List<MeterForecast> forecasts = ((ServiceAgreement)getProgramServiceAgreementEnrollment().getServiceAgreement()).getMeterForecasts();
        int i;
        MeterForecast forecast = getForecastByDate(forecasts, date); //REFERENCE LOAD FORECAST BY SA AND HOUR
        if (forecast != null) {
            Long[] endHours = forecast.getHourEndsAsArray();
            if (hourEnd == null) {
                for (i = 0; i < endHours.length; i++) { //TRAVERSE END HOURS MEASUREMENT
                    totalForecastCalculation = getTotalForecastCalculation(totalForecastCalculation, fsls, forecast, endHours[i]);
                }
            } else {
                totalForecastCalculation = getTotalForecastCalculation(totalForecastCalculation, fsls, forecast, endHours[hourEnd.getHourNumber() - 1]);
            }
        }
        return totalForecastCalculation;
    }

    @Transient
    public IsoResource getActiveResource() {
        if (!registrations.isEmpty()) {
            for (RegistrationSubmissionStatus registration : registrations) {
                if (registration.isActive()) {
                    return registration.getIsoResource();
                }
            }
        }
        return null;
    }
    @Transient
    public RegistrationSubmissionStatus getLastRegistration() {
        RegistrationSubmissionStatus lastRegistration;
        if (CollectionUtils.isNotEmpty(registrations)) {
            lastRegistration = registrations.get(0);
            for (RegistrationSubmissionStatus registration : registrations) {
                if (lastRegistration.getActiveEndDate() == null || lastRegistration.getActiveEndDate().before(registration.getActiveEndDate()) ) {
                    lastRegistration = registration;
                }
            }
            return lastRegistration;
        }
        return null;
    }

    private MeterForecast getForecastByDate(List<MeterForecast> forecasts, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        for (MeterForecast forecast : forecasts) {
            if (MeterForecast.REFERENCCE_LOAD.equals(forecast.getMeasureType())) {
                LocalDate forecastDate = forecast.getMeasureDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (forecastDate.isEqual(localDate)) {
                    return forecast;
                }
            }
        }
        return null;
    }

    private long getTotalForecastCalculation(long totalForecastCalculation, List<ProgramFirmServiceLevel> fsls, MeterForecast forecast, long partialForecastCalculation) {
        for (ProgramFirmServiceLevel fsl : fsls) { //SUBSTRACT ALL ACTIVE EXISTING FSL
            if (fslActiveAtEndHour(fsl.getEffectiveStartDate(), fsl.getEffectiveEndDate(), forecast.getMeasureDate())) {
                partialForecastCalculation -= (fsl.getValue().intValue() * ConstantsProviderModel.KW_PRECISION); //FSL IS DECIMAL
            }
        }
        if (partialForecastCalculation > 0) { //PARCIAL FORECAST CAN NOT BE 0
            totalForecastCalculation += partialForecastCalculation;
        }
        return totalForecastCalculation;
    }


    private boolean fslActiveAtEndHour(Date effectiveStartDate, Date effectiveEndDate, Date measureDate) {
        LocalDate startDate = effectiveStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate mDate = measureDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (effectiveEndDate != null) {
            LocalDate endDate = effectiveEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return (mDate.isEqual(startDate) || mDate.isAfter(startDate)) && mDate.isBefore(endDate);
        } else {
            return mDate.isEqual(startDate) || mDate.isAfter(startDate);
        }
    }

    public boolean isPendingToUnregister() {
        return status.equals(LocationStatus.PENDING_TO_UNREGISTER.getText()) || status.equals(LocationStatus.NON_REGISTRABLE);
    }
}
