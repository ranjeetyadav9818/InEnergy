package com.inenergis.entity.locationRegistration;


import com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("Location")
public class LocationSubmissionException extends IsoException {

    @ManyToOne
    @JoinColumn(name = "LRS_ID")
    private LocationSubmissionStatus locationSubmissionStatus;

    @Column(name = "CAN_CONTINUE")
    private boolean canContinue;

    @Column(name = "MARKED_RETRY")
    private boolean markedRetry;

    public static final String EXCEPTION_SEPARATOR = ", ";

    public enum LocationSubmissionExceptionTypes {

        LSE_ID_NOT_AVAILABLE("LSE_ID_NOT_AVAILABLE"),
        UDC_ID_NOT_AVAILABLE("UDC_ID_NOT_AVAILABLE"),
        DRP_ID_NOT_AVAILABLE("DRP_ID_NOT_AVAILABLE"),
        SC_ID_NOTDEFINED("SC_ID_NOT_DEFINED"),
        SUBLAP_NOT_DEFINED("SUBLAP_NOT_DEFINED"),

        METER_INTERVAL_TYPE_MISMATCH("METER_INTERVAL_TYPE_MISMATCH"), //TODO Decouple to a new subclass
        LOCATION_ATTRIBUTES_MISSING("LOCATION_ATTRIBUTES_MISSING");//TODO Decouple to a new subclass

        private final String text;

        LocationSubmissionExceptionTypes(final String text) {
            this.text = text;
        }

        public String toString() {
            return text;
        }

        public String getText() {
            return text;
        }

        public String getId() {return getDeclaringClass().getCanonicalName() + '.' + name();}

        //It should be better if each module (Processor etc.) has its own wf exception class
    }

    public enum DispositionType {

        REVIEWED("REVIEWED", Boolean.TRUE),
        NOT_REVIEWED("NOT REVIEWED", Boolean.FALSE);

        private final String text;
        private final Boolean value;

        DispositionType(final String text, Boolean value) {
            this.text = text;
            this.value = value;
        }

        public String toString() {
            return text;
        }

        public String getText() {
            return text;
        }

        public Boolean getValue() {
            return value;
        }
    }

    @Transient
    public String formatDisposition() {
        return isMarkedRetry() ? DispositionType.REVIEWED.getText() : DispositionType.NOT_REVIEWED.getText();
    }

    @Getter
    @Transient
    private List<String> accumulatedExceptions = new ArrayList<String>(LocationSubmissionExceptionTypes.values().length);

    public void addException(LocationSubmissionExceptionTypes typeToAdd) {
        if (! accumulatedExceptions.contains(typeToAdd.name())) {
            accumulatedExceptions.add(typeToAdd.name());
        }
    }

    public void onCreate(){
        setDateAdded(new Date());
        onModifyflushAccumulatedExceptions();
    }

    public void onModifyflushAccumulatedExceptions() {
        setType(StringUtils.join(accumulatedExceptions, EXCEPTION_SEPARATOR));
    }

    @PostLoad
    public void onload() {
        accumulatedExceptions = Arrays.asList(getType().split(EXCEPTION_SEPARATOR));
    }

    public boolean disputed(){
        if(locationSubmissionStatus != null && locationSubmissionStatus.getStatus() != null){
            LocationStatus locationStatus = LocationStatus.valueFromText(locationSubmissionStatus.getStatus().toUpperCase());
            return LocationStatus.DISPUTED.equals(locationStatus) || LocationStatus.DUPLICATED.equals(locationStatus);
        }
        return false;
    }
}
