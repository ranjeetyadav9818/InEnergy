package com.inenergis.util.soap;

import com.itron.mdm.curtailment._2008._05.CurtailmentEventSaveBatchResponse;
import com.itron.mdm.curtailment._2008._05.ICurtailmentEventSaveExecuteBatchSaveMdmServiceFaultFaultMessage;
import com.itron.mdm.curtailment._2008._05.ICurtailmentEventSaveIssueEventMdmServiceFaultFaultMessage;
import com.itron.mdm.curtailment._2008._05.IssueEventRequest;
import com.itron.mdm.curtailment._2008._05.IssueEventResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled //This is an integration test
public class ItronClientTest {

    private Properties properties = new Properties();
    private ItronClient itronClient;

    @BeforeEach
    public void before() throws Exception {
        properties.setProperty("itron.api.username", "MarketIntegrationDevWS");
        properties.setProperty("itron.api.password", "M1dWebS");
        properties.setProperty("itron.api.curtailmentEventSaveService.url", "https://pgetest.itron-hosting.com/IEEWebServices/V1/CurtailmentEventSaveService.svc");

        itronClient = new ItronClient(properties);
    }

    @Test
    public void testIssueEvent() throws ICurtailmentEventSaveIssueEventMdmServiceFaultFaultMessage, JAXBException, DatatypeConfigurationException {
        IssueEventRequest issueEventRequest = ItronHelper.issueEventRequestBuilder()
                .programId("DBPmatt")
                .correlationId("1001")
                .issueEventServicePointIdList(Arrays.asList("E3F459ED-38DF-4108-9A83-D296837C23E0", "E3F459ED-38DF-4108-9A83-D296837C23E1"))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .eventAlternateId("2" +
                        "")
                .build();

        IssueEventResponse response = itronClient.issueEvent(issueEventRequest);

        assertNotNull(response);
    }

    @Test
    public void test() throws ICurtailmentEventSaveExecuteBatchSaveMdmServiceFaultFaultMessage, DatatypeConfigurationException {
        IssueEventRequest issueEventRequest1 = ItronHelper.issueEventRequestBuilder()
                .programId("DBPmatt")
                .correlationId("1001")
                .issueEventServicePointIdList(Arrays.asList("E3F459ED-38DF-4108-9A83-D296837C23E0", "E3F459ED-38DF-4108-9A83-D296837C23E1"))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .eventAlternateId("1")
                .build();

        IssueEventRequest issueEventRequest2 = ItronHelper.issueEventRequestBuilder()
                .programId("DBPmatt")
                .correlationId("1002")
                .issueEventServicePointIdList(Arrays.asList("E3F459ED-38DF-4108-9A83-D296837C23E3", "E3F459ED-38DF-4108-9A83-D296837C23E4"))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .eventAlternateId("2")
                .build();

        String correlationId = Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        CurtailmentEventSaveBatchResponse response = itronClient.executeBatchSave(correlationId, Arrays.asList(issueEventRequest1, issueEventRequest2));

        assertNotNull(response);
    }
}