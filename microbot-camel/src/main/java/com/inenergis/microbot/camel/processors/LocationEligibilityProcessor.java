package com.inenergis.microbot.camel.processors;

import com.inenergis.entity.DataMapping;
import com.inenergis.entity.Premise;
import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.microbot.camel.exception.LocationProcessingException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class LocationEligibilityProcessor implements Processor {

    private final Logger log = LoggerFactory.getLogger(LocationEligibilityProcessor.class);

    private DataMapping lseData;
    private DataMapping subLapData;

    private IsoProduct isoProduct;
    private Premise premise;

    @Override
    public void process(Exchange exchange) throws Exception {

        ProgramServiceAgreementEnrollment enrollment = (ProgramServiceAgreementEnrollment) exchange.getProperty("enrollment");

        lseData = (DataMapping) exchange.getProperty("lseData");
        subLapData = (DataMapping) exchange.getProperty("subLapData");

        Program program = enrollment.getProgram();
        if (program == null) {
            throw new LocationProcessingException(String.format("Can't get Program for Enrollment (id=%d)", enrollment.getId()));
        }

        ProgramProfile programProfile = program.getActiveProfile();
        if (programProfile == null) {
            throw new LocationProcessingException(String.format("Can't get Active Profile for Program (id=%d)", program.getId()));
        }

        // #1 Check if CAISO program is eligible
        if (!programProfile.isWholesaleMarketEligible() || !programProfile.isWholesaleParticipationActive()) {
            throw new LocationProcessingException(String.format("CAISO program profile (id=%d) is not eligible", programProfile.getId()));
        }

        isoProduct = enrollment.getProgram().getActiveProfile().getWholesaleIsoProduct();
        if (isoProduct == null) {
            throw new Exception(String.format("Can't get Wholesale ISO Product for Profile (id=%d)", programProfile.getId()));
        }

        Iso iso = isoProduct.getProfile().getIso();

        try {
            premise = enrollment.getServiceAgreement().getAgreementPointMaps().get(0).getServicePoint().getPremise();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            throw new LocationProcessingException("Can't get Premise");
        }

        LocationSubmissionStatus locationSubmissionStatus = getLocationSubmissionStatus(enrollment);
        LocationSubmissionException exception = checkExceptions(enrollment, locationSubmissionStatus);
        if (exception != null) {
            exception.onCreate();
            locationSubmissionStatus.setExceptions(Collections.singletonList(exception));
            exception.getAccumulatedExceptions().forEach(log::error);
        } else {
            locationSubmissionStatus.setExceptions(Collections.emptyList());
        }
        locationSubmissionStatus.setIso(iso);
        exchange.getIn().setBody(locationSubmissionStatus);

        exchange.setProperty("isLocationEligible", exception == null);
    }

    private LocationSubmissionStatus getLocationSubmissionStatus(ProgramServiceAgreementEnrollment enrollment) {
        LocationSubmissionStatus locationSubmissionStatus = enrollment.getLastLocation();
        if (locationSubmissionStatus == null) {
            locationSubmissionStatus = new LocationSubmissionStatus();
            locationSubmissionStatus.setProgramServiceAgreementEnrollment(enrollment);
        }
        return locationSubmissionStatus;
    }

    private LocationSubmissionException checkExceptions(ProgramServiceAgreementEnrollment enrollment, LocationSubmissionStatus locationSubmissionStatus) {
        LocationSubmissionException locationSubmissionException = new LocationSubmissionException();
        final boolean subLapNotSetManually = StringUtils.isEmpty(locationSubmissionStatus.getIsoSublap ());
        // #2 Check if LSE ID is available
        if (lseData == null || lseData.getDestinationCode() == null) {
            locationSubmissionException.addException(LocationSubmissionException.LocationSubmissionExceptionTypes.LSE_ID_NOT_AVAILABLE);
        }

        // #3 Check UDC ID is available
        if (isoProduct.getProfile().getUdcId() == null) {
            locationSubmissionException.addException(LocationSubmissionException.LocationSubmissionExceptionTypes.UDC_ID_NOT_AVAILABLE);
        }

        // #4 Check DRP ID is available
        if (isoProduct.getProfile().getDrpId() == null) {
            locationSubmissionException.addException(LocationSubmissionException.LocationSubmissionExceptionTypes.DRP_ID_NOT_AVAILABLE);
        }

        // #5 Check if Sub Lap defined
        if (subLapNotSetManually && (subLapData == null || subLapData.getDestinationCode() == null)) {
            locationSubmissionException.addException(LocationSubmissionException.LocationSubmissionExceptionTypes.SUBLAP_NOT_DEFINED);
        }

        // #6 Check if Sub Lap defined
        if (isoProduct.getProfile().getScId() == null) {
            locationSubmissionException.addException(LocationSubmissionException.LocationSubmissionExceptionTypes.SC_ID_NOTDEFINED);
        }

        // #7 Check Service Account Number
        if (enrollment.getServiceAgreement().getSaUuid() == null || premise.getServiceAddress1() == null) {
            locationSubmissionException.addException(LocationSubmissionException.LocationSubmissionExceptionTypes.LOCATION_ATTRIBUTES_MISSING);
        }

        if (locationSubmissionException.getAccumulatedExceptions().isEmpty()) {
            locationSubmissionStatus.setIsoLse(lseData.getDestinationCode());
            if (subLapNotSetManually) {
                locationSubmissionStatus.setIsoSublap (subLapData.getDestinationCode());
            }
            return null;
        }
        locationSubmissionException.setLocationSubmissionStatus(locationSubmissionStatus);
        locationSubmissionException.setMarkedRetry(false);
        return locationSubmissionException;
    }
}