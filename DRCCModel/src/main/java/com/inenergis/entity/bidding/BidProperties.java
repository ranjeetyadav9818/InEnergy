package com.inenergis.entity.bidding;

import com.inenergis.entity.genericEnum.RiskType;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.entity.program.ProgramProfile;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BidProperties implements Serializable {
    private RiskType capacityRisk;
    private RiskType programRisk;
    private RiskType customerFatigueRisk;
    private Long resourceAdequacyPotential;
    private String availableMarketHours;

    private RegistrationSubmissionStatus registration;

    String scheduleType;

    BidProperties(RegistrationSubmissionStatus registration) {
        this.registration = registration;
    }

    BidProperties(RegistrationSubmissionStatus registration, Date tradeDate) {
        this(registration);
        reCalculateResourceAdequacyPotential(tradeDate);
    }

    public Long calculateResourceAdequacyPotential(RegistrationSubmissionStatus registration, Date tradeDate) {
        Long totalForecastedCapacityWatts = 0L;
        if (registration != null) {
            for (LocationSubmissionStatus locationSubmissionStatus : registration.getLocations()) {
                for (HourEnd hourEnd : HourEnd.values()) {
                    ProgramProfile activeProfile = locationSubmissionStatus.getProgram().getActiveProfile();
                    if (activeProfile != null && activeProfile.getDefaultBidHoursFrom() != null && activeProfile.getDefaultBidHoursTo() != null) {
                        Integer hourNumberFrom = activeProfile.getDefaultBidHoursFrom().getHourNumber();
                        Integer hourNumberTo = activeProfile.getDefaultBidHoursTo().getHourNumber();
                        if (hourEnd.getHourNumber() >= hourNumberFrom && hourEnd.getHourNumber() <= hourNumberTo) {
                            totalForecastedCapacityWatts = Long.sum(totalForecastedCapacityWatts, locationSubmissionStatus.getCalculatedCapacity(tradeDate, hourEnd));
                        }
                    }
                }
            }
        }
        return totalForecastedCapacityWatts;
    }

    public void reCalculateResourceAdequacyPotential(Date tradeDate) {
        resourceAdequacyPotential = calculateResourceAdequacyPotential(registration, tradeDate);
    }

    public boolean allRisksAreLow() {
        return programRisk.equals(RiskType.Low) && customerFatigueRisk.equals(RiskType.Low) && capacityRisk.equals(RiskType.Low);
    }
}