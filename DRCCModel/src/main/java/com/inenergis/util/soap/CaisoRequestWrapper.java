package com.inenergis.util.soap;

import com.caiso.ads.api.model.APIDispatchResponseType;
import com.caiso.ads.api.model.APITrajectoryResponseType;
import com.caiso.ads.api.model.DispatchBatchType;
import com.caiso.ads.api.model.ObjectFactory;
import com.caiso.ads.api.model.ObjectFactoryDispatchDetails;
import com.caiso.ads.api.model.ObjectFactoryTrajectory;
import com.caiso.ads.api.webservices.dispatch.v7.APIWebService;
import com.caiso.ads.api.webservices.dispatch.v7.APIWebService_Service;
import com.caiso.soa.batchvalidationstatus_v1.BatchValidationStatus;
import com.caiso.soa.drregistrationdata_v1.DRRegistrationData;
import com.caiso.soa.requestdrregistrationdata_v1.RequestDRRegistrationData;
import com.caiso.soa.retrievebatchvalidationstatus_v1_wsdl.FaultReturnType;
import com.caiso.soa.retrievebatchvalidationstatus_v1_wsdl.RetrieveBatchValidationStatusV1;
import com.caiso.soa.retrievebatchvalidationstatus_v1_wsdl.RetrieveBatchValidationStatusV1Service;
import com.caiso.soa.retrievedrlocations_v1_wsdl.RetrieveDRLocationsV1;
import com.caiso.soa.retrievedrlocations_v1_wsdl.RetrieveDRLocationsV1Service;
import com.caiso.soa.retrievedrregistrations_v1_wsdl.RetrieveDRRegistrationsV1;
import com.caiso.soa.retrievedrregistrations_v1_wsdl.RetrieveDRRegistrationsV1Service;
import com.caiso.soa.service.CaisoNetworkHelper;
import com.caiso.soa.standardoutput_v1.StandardOutput;
import com.caiso.soa.submitdrlocations_v1_wsdl.SubmitDRLocationsV1;
import com.caiso.soa.submitdrlocations_v1_wsdl.SubmitDRLocationsV1Service;
import com.caiso.soa.submitdrregistrations_v1_wsdl.SubmitDRRegistrationsV1;
import com.caiso.soa.submitdrregistrations_v1_wsdl.SubmitDRRegistrationsV1Service;
import org.apache.camel.StringSource;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Properties;


public class CaisoRequestWrapper {

    private Properties properties;

    public CaisoRequestWrapper(Properties properties) {
        this.properties = properties;
    }

    protected static final Logger log = LoggerFactory.getLogger(CaisoRequestWrapper.class);

    public DRRegistrationData retrieveLocation(RequestDRRegistrationData request) throws com.caiso.soa.retrievedrlocations_v1_wsdl.FaultReturnType {
        RetrieveDRLocationsV1Service service = new RetrieveDRLocationsV1Service((URL) null);
        RetrieveDRLocationsV1 servicePort = service.getRetrieveDRLocationsServicePort();
        Client client = ClientProxy.getClient(servicePort);
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, properties.getProperty("caiso.retrieveLocation.url"));
        prepareRetrieveMessage(client);
        servicePort.retrieveDRLocationsV1(request);
        return getDrRegistrationData(client);
    }

    public DRRegistrationData retrieveRegistration(RequestDRRegistrationData request) throws com.caiso.soa.retrievedrregistrations_v1_wsdl.FaultReturnType {
        RetrieveDRRegistrationsV1Service service = new RetrieveDRRegistrationsV1Service((URL) null);
        RetrieveDRRegistrationsV1 servicePort = service.getRetrieveDRRegistrationsServicePort();
        Client client = ClientProxy.getClient(servicePort);
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, properties.getProperty("caiso.retrieveRegistration.url"));
        prepareRetrieveMessage(client);
        servicePort.retrieveDRRegistrationsV1(request);
        return getDrRegistrationData(client);
    }

    public BatchValidationStatus retrieveBatchStatus(BatchValidationStatus batchStatus) throws FaultReturnType {
        RetrieveBatchValidationStatusV1Service service = new RetrieveBatchValidationStatusV1Service((URL) null);
        RetrieveBatchValidationStatusV1 servicePort = service.getRetrieveBatchValidationStatusServicePort();
        Client client = ClientProxy.getClient(servicePort);
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, properties.getProperty("caiso.retrieveBatch.url"));
        prepareRetrieveMessage(client);
        servicePort.retrieveBatchValidationStatusV1(batchStatus);
        return getBatchData(client);
    }

    public StandardOutput submitLocation(String zippedXml) throws com.caiso.soa.submitdrlocations_v1_wsdl.FaultReturnType {
        try{
            SubmitDRLocationsV1Service service = new SubmitDRLocationsV1Service((URL) null);
            SubmitDRLocationsV1 servicePort = service.getSubmitDRLocationsServicePort();
            Client client = ClientProxy.getClient(servicePort);
            client.getRequestContext().put(Message.ENDPOINT_ADDRESS, properties.getProperty("caiso.submitLocation.url"));
            prepareSubmitMessage(zippedXml, client);
            StandardOutput standardOutput = servicePort.submitDRLocationsV1(null);
            return standardOutput;
        }catch (Exception e){
            throw e;
        }
    }

    public StandardOutput submitRegistration(String zippedXml) throws com.caiso.soa.submitdrregistrations_v1_wsdl.FaultReturnType {
        SubmitDRRegistrationsV1Service service = new SubmitDRRegistrationsV1Service((URL) null);
        SubmitDRRegistrationsV1 servicePort = service.getSubmitDRRegistrationsServicePort();
        Client client = ClientProxy.getClient(servicePort);
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, properties.getProperty("caiso.submitRegistration.url"));
        prepareSubmitMessage(zippedXml, client);
        StandardOutput standardOutput = servicePort.submitDRRegistrationsV1(null);
        return standardOutput;
    }

    public APIDispatchResponseType getBatchesSinceUUID(String batchUUID) throws Exception {
        APIWebService servicePort = getADSAPIWebService();
        String result = servicePort.getDispatchBatchesSinceUID(batchUUID);

        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        StringReader reader = new StringReader(result);
        APIDispatchResponseType response = ((JAXBElement<APIDispatchResponseType>) jaxbContext.createUnmarshaller().unmarshal(reader)).getValue();
        return response;
    }

    public Pair<String,DispatchBatchType> getDispatchBatch(String batchUUID) throws Exception {
        APIWebService servicePort = getADSAPIWebService();
        String result = servicePort.getDispatchBatch(batchUUID);

        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactoryDispatchDetails.class);
        InputStream adsBatchDetails = CaisoNetworkHelper.getADSBatchDetails(result);
        String extractedResult = IOUtils.toString(adsBatchDetails);
        DispatchBatchType response = ((JAXBElement<DispatchBatchType>) jaxbContext.createUnmarshaller().unmarshal(new StringReader(extractedResult))).getValue();
        return ImmutablePair.of(extractedResult,response);
    }

    public Pair<String,APITrajectoryResponseType> getTrajectoryData(String batchUUID) throws Exception {
        APIWebService servicePort = getADSAPIWebService();
        String result = servicePort.getTrajectoryData(batchUUID);
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactoryTrajectory.class);
        InputStream adsBatchDetails = CaisoNetworkHelper.getADSBatchDetails(result);
        String extractedResult = IOUtils.toString(adsBatchDetails);
        APITrajectoryResponseType response = ((JAXBElement<APITrajectoryResponseType>) jaxbContext.createUnmarshaller().unmarshal(new StringReader(extractedResult))).getValue();
        return ImmutablePair.of(extractedResult,response);
    }

    public boolean isNewTrajectoryData(String batchUUID) throws Exception {
        APIWebService servicePort = getADSAPIWebService();
        boolean newTrajData = servicePort.isNewTrajData(batchUUID);
        return newTrajData;
    }

    public void validateBatch(String batchUUID) throws Exception {
        APIWebService servicePort = getADSAPIWebService();
        servicePort.validateDispatchBatch(batchUUID);
    }

    private APIWebService getADSAPIWebService() throws Exception {
        APIWebService_Service service = new APIWebService_Service((URL) null);
        APIWebService servicePort = service.getAPIWebServicePort();
        Client client = ClientProxy.getClient(servicePort);
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, properties.getProperty("caiso.ads.url"));

        SoapRequestHelper.addSSL(client, properties.getProperty("caiso.keystore.location"), properties.getProperty("caiso.keystore.password"), properties.getProperty("caiso.keystore.type"));
        SoapRequestHelper.addLoggingToCommunication(client, true, true);
        return servicePort;
    }

    private void prepareSubmitMessage(String zippedXml, Client client) {
        try {
            prepareGenericMessage(client, "{Element}{http://www.caiso.com/soa/2006-09-30/CAISOWSHeader.xsd}CAISOWSHeader;{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body;" +
                    "{Element}{http://www.caiso.com/soa/2006-06-13/StandardAttachmentInfor.xsd}standardAttachmentInfor;{Element}{http://www.caiso.com/mrtu/soa/schemas/2005/09/attachmenthash}attachmentHash");

            CaisoNetworkHelper.addAttachmentInformation(client);

            CaisoNetworkHelper.addAttachmentHash(client, zippedXml);

            CaisoNetworkHelper.addAttachment(client, zippedXml, "http://www.caiso.com/soa/submitDRLocations_v1.wsdl");
        } catch (Exception e) {
            log.error("PLEASE CHECK - Error trying to create a request", e);
            throw new InternalSoapException(e);
        }
    }

    private void prepareRetrieveMessage(Client client) {
        try {
            prepareGenericMessage(client, "{Element}{http://www.caiso.com/soa/2006-09-30/CAISOWSHeader.xsd}CAISOWSHeader;{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body");
        } catch (Exception e) {
            log.error("PLEASE CHECK - Error trying to create a request", e);
            throw new InternalSoapException(e);
        }
    }

    private void prepareGenericMessage(Client client, String signatureParts) throws Exception {
        SoapRequestHelper.addSSL(client, properties.getProperty("caiso.keystore.location"), properties.getProperty("caiso.keystore.password"), properties.getProperty("caiso.keystore.type"));
        SoapRequestHelper.addLoggingToCommunication(client, true, true);

        SoapRequestHelper.addSignatureToHeader(client, signatureParts, properties.getProperty("caiso.keystore.alias"), properties.getProperty("caiso.keystore.properties.file"));
        CaisoNetworkHelper.addCaisoSecurityHeader(client, properties.getProperty("caiso.security.header"));
    }

    private DRRegistrationData getDrRegistrationData(Client client) {
        try {
            DRRegistrationData response = CaisoNetworkHelper.getDRResponse(client);
            return response;
        } catch (Exception e) {
            log.error("PLEASE CHECK - Error trying to read a response", e);
            throw new InternalSoapException(e);
        }
    }

    private BatchValidationStatus getBatchData(Client client) {
        try {
            BatchValidationStatus response = CaisoNetworkHelper.getBatchStatusResponse(client);
            return response;
        } catch (Exception e) {
            log.error("PLEASE CHECK - Error trying to read a response", e);
            throw new InternalSoapException(e);
        }
    }
}