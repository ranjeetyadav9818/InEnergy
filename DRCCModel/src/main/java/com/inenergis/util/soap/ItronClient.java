package com.inenergis.util.soap;

import com.itron.mdm.common._2008._04.ObjectFactory;
import com.itron.mdm.curtailment._2008._05.ArrayOfIssueEventRequest;
import com.itron.mdm.curtailment._2008._05.CurtailmentEventSave;
import com.itron.mdm.curtailment._2008._05.CurtailmentEventSaveBatchRequest;
import com.itron.mdm.curtailment._2008._05.CurtailmentEventSaveBatchResponse;
import com.itron.mdm.curtailment._2008._05.ICurtailmentEventSave;
import com.itron.mdm.curtailment._2008._05.ICurtailmentEventSaveExecuteBatchSaveMdmServiceFaultFaultMessage;
import com.itron.mdm.curtailment._2008._05.ICurtailmentEventSaveIssueEventMdmServiceFaultFaultMessage;
import com.itron.mdm.curtailment._2008._05.IssueEventRequest;
import com.itron.mdm.curtailment._2008._05.IssueEventResponse;
import com.itron.mdm.service.ItronNetworkHelper;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Properties;

public class ItronClient {

    private Properties properties;

    protected static final Logger log = LoggerFactory.getLogger(ItronClient.class);

    public ItronClient(Properties properties) {
        this.properties = properties;
    }

    public IssueEventResponse issueEvent(IssueEventRequest issueEventRequest) throws ICurtailmentEventSaveIssueEventMdmServiceFaultFaultMessage {
        final String mockItron = properties.getProperty("itron.mockItron");
        if (mockItron == null) {
        return getCurtailmentEventService().issueEvent(issueEventRequest);
        } else {
            return new IssueEventResponse();
        }
    }

    public CurtailmentEventSaveBatchResponse executeBatchSave(String correlationId, List<IssueEventRequest> issueEventRequests) throws ICurtailmentEventSaveExecuteBatchSaveMdmServiceFaultFaultMessage {
        CurtailmentEventSaveBatchRequest request = new CurtailmentEventSaveBatchRequest();

        ObjectFactory factory = new ObjectFactory();
        request.setCorrelationID(factory.createRequestBaseCorrelationID(correlationId));

        com.itron.mdm.curtailment._2008._05.ObjectFactory factory2 = new com.itron.mdm.curtailment._2008._05.ObjectFactory();
        ArrayOfIssueEventRequest arrayOfIssueEventRequest = factory2.createArrayOfIssueEventRequest();
        arrayOfIssueEventRequest.getIssueEventRequest().addAll(issueEventRequests);
        request.setIssueEventRequests(factory2.createArrayOfIssueEventRequest(arrayOfIssueEventRequest));

        return getCurtailmentEventService().executeBatchSave(request);
    }

    private void prepareRetrieveMessage(Client client) {
        try {
            prepareGenericMessage(client);
        } catch (Exception e) {
            log.error("PLEASE CHECK - Error trying to create a request", e);
            throw new InternalSoapException(e);
        }
    }

    private void prepareGenericMessage(Client client) throws Exception {
        ItronNetworkHelper.addItronSecurityHeader(client, properties);
        SoapRequestHelper.addLoggingToCommunication(client, true, true);
        SoapRequestHelper.disableTLSValidation(client);
    }

    private ICurtailmentEventSave getCurtailmentEventService() {
        CurtailmentEventSave curtailmentEventService = new CurtailmentEventSave((URL) null);
        ICurtailmentEventSave curtailmentEventSaveV1 = curtailmentEventService.getCurtailmentEventSaveV1();
        Client client = ClientProxy.getClient(curtailmentEventSaveV1);
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, properties.getProperty("itron.api.curtailmentEventSaveService.url"));
        prepareRetrieveMessage(client);

        return curtailmentEventSaveV1;
    }
}