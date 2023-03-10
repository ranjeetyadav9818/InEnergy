package com.inenergis.util.soap;

import com.inenergis.util.ConstantsProviderModel;
import com.itron.mdm.common._2008._04.ObjectFactory;
import com.itron.mdm.curtailment._2008._05.ArrayOfIssueEventServicePoint;
import com.itron.mdm.curtailment._2008._05.IDType;
import com.itron.mdm.curtailment._2008._05.IssueEventRequest;
import com.itron.mdm.curtailment._2008._05.IssueEventServicePoint;
import lombok.Builder;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ItronHelper {

    private ItronHelper() {
    }

    @Builder(builderMethodName = "issueEventRequestBuilder")
    public static IssueEventRequest createIssueEventRequest(String programId, String correlationId, List<String> issueEventServicePointIdList, LocalDateTime startDate, LocalDateTime endDate, String eventAlternateId) throws DatatypeConfigurationException {
        IssueEventRequest issueEventRequest = new IssueEventRequest();
        ObjectFactory factory = new ObjectFactory();
        com.itron.mdm.curtailment._2008._05.ObjectFactory factory2 = new com.itron.mdm.curtailment._2008._05.ObjectFactory();

        if (correlationId != null) {
            issueEventRequest.setCorrelationID(factory.createRequestBaseCorrelationID(correlationId));
        } else {
            issueEventRequest.setCorrelationID(factory.createRequestBaseCorrelationID(Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))));
        }

        if (eventAlternateId != null) {
            issueEventRequest.setEventAlternateID(factory2.createIssueEventRequestEventAlternateID(eventAlternateId));
        }

        if (startDate != null) {
            GregorianCalendar startDateCalendar = new GregorianCalendar();
            startDateCalendar.setTime(Date.from(startDate.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant()));
            issueEventRequest.setEventStartDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(startDateCalendar));
        }

        if (endDate != null) {
            GregorianCalendar endDateCalendar = new GregorianCalendar();
            endDateCalendar.setTime(Date.from(endDate.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant()));
            issueEventRequest.setEventEndDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(endDateCalendar));
        }

        if (programId != null) {
            issueEventRequest.setProgramID(programId);
        }

        if (issueEventServicePointIdList != null) {
            ArrayOfIssueEventServicePoint servicePoints = new ArrayOfIssueEventServicePoint();

            for (String issueEventServicePointId : issueEventServicePointIdList) {
                IssueEventServicePoint issueEventServicePoint = new IssueEventServicePoint();
                issueEventServicePoint.setID(factory2.createIssueEventServicePointID(issueEventServicePointId));
                issueEventServicePoint.setIDType(IDType.ACCOUNT_NUMBER);
                servicePoints.getIssueEventServicePoint().add(issueEventServicePoint);
            }

            if (!servicePoints.getIssueEventServicePoint().isEmpty()) {
                issueEventRequest.setIssueEventServicePoints(servicePoints);
            }
        }

        return issueEventRequest;
    }
}