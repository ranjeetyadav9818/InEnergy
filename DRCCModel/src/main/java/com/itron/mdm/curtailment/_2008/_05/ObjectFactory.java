
package com.itron.mdm.curtailment._2008._05;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.itron.mdm.curtailment._2008._05 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ArrayOfIssueEventRequest_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ArrayOfIssueEvent_Request");
    private final static QName _ArrayOfIssueEventResponse_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ArrayOfIssueEvent_Response");
    private final static QName _ArrayOfIssueEventBlock_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ArrayOfIssueEventBlock");
    private final static QName _ArrayOfIssueEventServicePoint_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ArrayOfIssueEventServicePoint");
    private final static QName _ArrayOfManageEventRequest_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ArrayOfManageEvent_Request");
    private final static QName _ArrayOfManageEventResponse_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ArrayOfManageEvent_Response");
    private final static QName _ArrayOfServicePoint_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ArrayOfServicePoint");
    private final static QName _CurtailmentEventSaveBatchRequest_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "CurtailmentEventSave_BatchRequest");
    private final static QName _CurtailmentEventSaveBatchResponse_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "CurtailmentEventSave_BatchResponse");
    private final static QName _IDType_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IDType");
    private final static QName _IssueEventRequestTEST_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IssueEvent_Request_TEST");
    private final static QName _IssueEventResponse_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IssueEvent_Response");
    private final static QName _IssueEventBlock_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IssueEventBlock");
    private final static QName _IssueEventServicePoint_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IssueEventServicePoint");
    private final static QName _ManageEventRequestTEST_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ManageEvent_Request_TEST");
    private final static QName _ManageEventResponse_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ManageEvent_Response");
    private final static QName _ManageEventAction_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ManageEventAction");
    private final static QName _ServicePoint_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ServicePoint");
    private final static QName _SettlementPeriod_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "SettlementPeriod");
    private final static QName _ExecuteBatchSaveRequest_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "request");
    private final static QName _ExecuteBatchSaveResponseExecuteBatchSaveResult_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ExecuteBatchSaveResult");
    private final static QName _IssueEventResponse2IssueEventResult_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IssueEventResult");
    private final static QName _ManageEventResponse2ManageEventResult_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ManageEventResult");
    private final static QName _ServicePointAccountID_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "AccountID");
    private final static QName _ServicePointAccountNumber_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "AccountNumber");
    private final static QName _ServicePointServicePointID_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ServicePointID");
    private final static QName _ManageEventRequestCallbackAddress_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "CallbackAddress");
    private final static QName _ManageEventRequestEndDateTime_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "EndDateTime");
    private final static QName _ManageEventRequestEventAlternateID_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "EventAlternateID");
    private final static QName _ManageEventRequestEventID_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "EventID");
    private final static QName _ManageEventRequestRequestingSystem_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "RequestingSystem");
    private final static QName _ManageEventRequestStartDateTime_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "StartDateTime");
    private final static QName _ManageEventRequestTerminationDateTime_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "TerminationDateTime");
    private final static QName _IssueEventServicePointAdjustmentFactor_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "AdjustmentFactor");
    private final static QName _IssueEventServicePointCLPMonthly_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "CLPMonthly");
    private final static QName _IssueEventServicePointID_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ID");
    private final static QName _IssueEventServicePointMaximumReduction_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "MaximumReduction");
    private final static QName _IssueEventServicePointMessage_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "Message");
    private final static QName _IssueEventBlockAdditionalReductionPrice_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "AdditionalReductionPrice");
    private final static QName _IssueEventBlockBlockPrice_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "BlockPrice");
    private final static QName _IssueEventBlockPenaltyPrice_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "PenaltyPrice");
    private final static QName _IssueEventResponseAlternateID_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "AlternateID");
    private final static QName _IssueEventRequestContinueOnServicePointValidationFailure_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ContinueOnServicePointValidationFailure");
    private final static QName _IssueEventRequestDelayNoticeUntillDateTime_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "DelayNoticeUntillDateTime");
    private final static QName _IssueEventRequestDemandCapValue_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "DemandCapValue");
    private final static QName _IssueEventRequestIncludeServicePointsInOverlappingEvents_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IncludeServicePointsInOverlappingEvents");
    private final static QName _IssueEventRequestIsTestEvent_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IsTestEvent");
    private final static QName _IssueEventRequestRespondByDateTime_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "RespondByDateTime");
    private final static QName _IssueEventRequestStartDialingMetersDateTime_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "StartDialingMetersDateTime");
    private final static QName _CurtailmentEventSaveBatchResponseIssueEventResponses_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IssueEvent_Responses");
    private final static QName _CurtailmentEventSaveBatchResponseManageEventResponses_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ManageEvent_Responses");
    private final static QName _CurtailmentEventSaveBatchRequestIssueEventRequests_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "IssueEvent_Requests");
    private final static QName _CurtailmentEventSaveBatchRequestManageEventRequests_QNAME = new QName("http://www.itron.com/mdm/curtailment/2008/05", "ManageEvent_Requests");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.itron.mdm.curtailment._2008._05
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArrayOfIssueEventRequest }
     * 
     */
    public ArrayOfIssueEventRequest createArrayOfIssueEventRequest() {
        return new ArrayOfIssueEventRequest();
    }

    /**
     * Create an instance of {@link ArrayOfIssueEventResponse }
     * 
     */
    public ArrayOfIssueEventResponse createArrayOfIssueEventResponse() {
        return new ArrayOfIssueEventResponse();
    }

    /**
     * Create an instance of {@link ArrayOfIssueEventBlock }
     * 
     */
    public ArrayOfIssueEventBlock createArrayOfIssueEventBlock() {
        return new ArrayOfIssueEventBlock();
    }

    /**
     * Create an instance of {@link ArrayOfIssueEventServicePoint }
     * 
     */
    public ArrayOfIssueEventServicePoint createArrayOfIssueEventServicePoint() {
        return new ArrayOfIssueEventServicePoint();
    }

    /**
     * Create an instance of {@link ArrayOfManageEventRequest }
     * 
     */
    public ArrayOfManageEventRequest createArrayOfManageEventRequest() {
        return new ArrayOfManageEventRequest();
    }

    /**
     * Create an instance of {@link ArrayOfManageEventResponse }
     * 
     */
    public ArrayOfManageEventResponse createArrayOfManageEventResponse() {
        return new ArrayOfManageEventResponse();
    }

    /**
     * Create an instance of {@link ArrayOfServicePoint }
     * 
     */
    public ArrayOfServicePoint createArrayOfServicePoint() {
        return new ArrayOfServicePoint();
    }

    /**
     * Create an instance of {@link CurtailmentEventSaveBatchRequest }
     * 
     */
    public CurtailmentEventSaveBatchRequest createCurtailmentEventSaveBatchRequest() {
        return new CurtailmentEventSaveBatchRequest();
    }

    /**
     * Create an instance of {@link CurtailmentEventSaveBatchResponse }
     * 
     */
    public CurtailmentEventSaveBatchResponse createCurtailmentEventSaveBatchResponse() {
        return new CurtailmentEventSaveBatchResponse();
    }

    /**
     * Create an instance of {@link ExecuteBatchSave }
     * 
     */
    public ExecuteBatchSave createExecuteBatchSave() {
        return new ExecuteBatchSave();
    }

    /**
     * Create an instance of {@link ExecuteBatchSaveResponse }
     * 
     */
    public ExecuteBatchSaveResponse createExecuteBatchSaveResponse() {
        return new ExecuteBatchSaveResponse();
    }

    /**
     * Create an instance of {@link IssueEvent }
     * 
     */
    public IssueEvent createIssueEvent() {
        return new IssueEvent();
    }

    /**
     * Create an instance of {@link IssueEventRequest }
     * 
     */
    public IssueEventRequest createIssueEventRequest() {
        return new IssueEventRequest();
    }

    /**
     * Create an instance of {@link IssueEventResponse }
     * 
     */
    public IssueEventResponse createIssueEventResponse() {
        return new IssueEventResponse();
    }

    /**
     * Create an instance of {@link IssueEventBlock }
     * 
     */
    public IssueEventBlock createIssueEventBlock() {
        return new IssueEventBlock();
    }

    /**
     * Create an instance of {@link IssueEventResponse2 }
     * 
     */
    public IssueEventResponse2 createIssueEventResponse2() {
        return new IssueEventResponse2();
    }

    /**
     * Create an instance of {@link IssueEventServicePoint }
     * 
     */
    public IssueEventServicePoint createIssueEventServicePoint() {
        return new IssueEventServicePoint();
    }

    /**
     * Create an instance of {@link ManageEvent }
     * 
     */
    public ManageEvent createManageEvent() {
        return new ManageEvent();
    }

    /**
     * Create an instance of {@link ManageEventRequest }
     * 
     */
    public ManageEventRequest createManageEventRequest() {
        return new ManageEventRequest();
    }

    /**
     * Create an instance of {@link ManageEventResponse }
     * 
     */
    public ManageEventResponse createManageEventResponse() {
        return new ManageEventResponse();
    }

    /**
     * Create an instance of {@link ManageEventResponse2 }
     * 
     */
    public ManageEventResponse2 createManageEventResponse2() {
        return new ManageEventResponse2();
    }

    /**
     * Create an instance of {@link ServicePoint }
     * 
     */
    public ServicePoint createServicePoint() {
        return new ServicePoint();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIssueEventRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ArrayOfIssueEvent_Request")
    public JAXBElement<ArrayOfIssueEventRequest> createArrayOfIssueEventRequest(ArrayOfIssueEventRequest value) {
        return new JAXBElement<ArrayOfIssueEventRequest>(_ArrayOfIssueEventRequest_QNAME, ArrayOfIssueEventRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIssueEventResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ArrayOfIssueEvent_Response")
    public JAXBElement<ArrayOfIssueEventResponse> createArrayOfIssueEventResponse(ArrayOfIssueEventResponse value) {
        return new JAXBElement<ArrayOfIssueEventResponse>(_ArrayOfIssueEventResponse_QNAME, ArrayOfIssueEventResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIssueEventBlock }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ArrayOfIssueEventBlock")
    public JAXBElement<ArrayOfIssueEventBlock> createArrayOfIssueEventBlock(ArrayOfIssueEventBlock value) {
        return new JAXBElement<ArrayOfIssueEventBlock>(_ArrayOfIssueEventBlock_QNAME, ArrayOfIssueEventBlock.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIssueEventServicePoint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ArrayOfIssueEventServicePoint")
    public JAXBElement<ArrayOfIssueEventServicePoint> createArrayOfIssueEventServicePoint(ArrayOfIssueEventServicePoint value) {
        return new JAXBElement<ArrayOfIssueEventServicePoint>(_ArrayOfIssueEventServicePoint_QNAME, ArrayOfIssueEventServicePoint.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfManageEventRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ArrayOfManageEvent_Request")
    public JAXBElement<ArrayOfManageEventRequest> createArrayOfManageEventRequest(ArrayOfManageEventRequest value) {
        return new JAXBElement<ArrayOfManageEventRequest>(_ArrayOfManageEventRequest_QNAME, ArrayOfManageEventRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfManageEventResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ArrayOfManageEvent_Response")
    public JAXBElement<ArrayOfManageEventResponse> createArrayOfManageEventResponse(ArrayOfManageEventResponse value) {
        return new JAXBElement<ArrayOfManageEventResponse>(_ArrayOfManageEventResponse_QNAME, ArrayOfManageEventResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfServicePoint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ArrayOfServicePoint")
    public JAXBElement<ArrayOfServicePoint> createArrayOfServicePoint(ArrayOfServicePoint value) {
        return new JAXBElement<ArrayOfServicePoint>(_ArrayOfServicePoint_QNAME, ArrayOfServicePoint.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CurtailmentEventSaveBatchRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "CurtailmentEventSave_BatchRequest")
    public JAXBElement<CurtailmentEventSaveBatchRequest> createCurtailmentEventSaveBatchRequest(CurtailmentEventSaveBatchRequest value) {
        return new JAXBElement<CurtailmentEventSaveBatchRequest>(_CurtailmentEventSaveBatchRequest_QNAME, CurtailmentEventSaveBatchRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CurtailmentEventSaveBatchResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "CurtailmentEventSave_BatchResponse")
    public JAXBElement<CurtailmentEventSaveBatchResponse> createCurtailmentEventSaveBatchResponse(CurtailmentEventSaveBatchResponse value) {
        return new JAXBElement<CurtailmentEventSaveBatchResponse>(_CurtailmentEventSaveBatchResponse_QNAME, CurtailmentEventSaveBatchResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IDType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IDType")
    public JAXBElement<IDType> createIDType(IDType value) {
        return new JAXBElement<IDType>(_IDType_QNAME, IDType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IssueEventRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IssueEvent_Request_TEST")
    public JAXBElement<IssueEventRequest> createIssueEventRequestTEST(IssueEventRequest value) {
        return new JAXBElement<IssueEventRequest>(_IssueEventRequestTEST_QNAME, IssueEventRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IssueEventResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IssueEvent_Response")
    public JAXBElement<IssueEventResponse> createIssueEventResponse(IssueEventResponse value) {
        return new JAXBElement<IssueEventResponse>(_IssueEventResponse_QNAME, IssueEventResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IssueEventBlock }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IssueEventBlock")
    public JAXBElement<IssueEventBlock> createIssueEventBlock(IssueEventBlock value) {
        return new JAXBElement<IssueEventBlock>(_IssueEventBlock_QNAME, IssueEventBlock.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IssueEventServicePoint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IssueEventServicePoint")
    public JAXBElement<IssueEventServicePoint> createIssueEventServicePoint(IssueEventServicePoint value) {
        return new JAXBElement<IssueEventServicePoint>(_IssueEventServicePoint_QNAME, IssueEventServicePoint.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageEventRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ManageEvent_Request_TEST")
    public JAXBElement<ManageEventRequest> createManageEventRequestTEST(ManageEventRequest value) {
        return new JAXBElement<ManageEventRequest>(_ManageEventRequestTEST_QNAME, ManageEventRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageEventResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ManageEvent_Response")
    public JAXBElement<ManageEventResponse> createManageEventResponse(ManageEventResponse value) {
        return new JAXBElement<ManageEventResponse>(_ManageEventResponse_QNAME, ManageEventResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageEventAction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ManageEventAction")
    public JAXBElement<ManageEventAction> createManageEventAction(ManageEventAction value) {
        return new JAXBElement<ManageEventAction>(_ManageEventAction_QNAME, ManageEventAction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServicePoint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ServicePoint")
    public JAXBElement<ServicePoint> createServicePoint(ServicePoint value) {
        return new JAXBElement<ServicePoint>(_ServicePoint_QNAME, ServicePoint.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SettlementPeriod }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "SettlementPeriod")
    public JAXBElement<SettlementPeriod> createSettlementPeriod(SettlementPeriod value) {
        return new JAXBElement<SettlementPeriod>(_SettlementPeriod_QNAME, SettlementPeriod.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CurtailmentEventSaveBatchRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "request", scope = ExecuteBatchSave.class)
    public JAXBElement<CurtailmentEventSaveBatchRequest> createExecuteBatchSaveRequest(CurtailmentEventSaveBatchRequest value) {
        return new JAXBElement<CurtailmentEventSaveBatchRequest>(_ExecuteBatchSaveRequest_QNAME, CurtailmentEventSaveBatchRequest.class, ExecuteBatchSave.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CurtailmentEventSaveBatchResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ExecuteBatchSaveResult", scope = ExecuteBatchSaveResponse.class)
    public JAXBElement<CurtailmentEventSaveBatchResponse> createExecuteBatchSaveResponseExecuteBatchSaveResult(CurtailmentEventSaveBatchResponse value) {
        return new JAXBElement<CurtailmentEventSaveBatchResponse>(_ExecuteBatchSaveResponseExecuteBatchSaveResult_QNAME, CurtailmentEventSaveBatchResponse.class, ExecuteBatchSaveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IssueEventRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "request", scope = IssueEvent.class)
    public JAXBElement<IssueEventRequest> createIssueEventRequest(IssueEventRequest value) {
        return new JAXBElement<IssueEventRequest>(_ExecuteBatchSaveRequest_QNAME, IssueEventRequest.class, IssueEvent.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IssueEventResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IssueEventResult", scope = IssueEventResponse2 .class)
    public JAXBElement<IssueEventResponse> createIssueEventResponse2IssueEventResult(IssueEventResponse value) {
        return new JAXBElement<IssueEventResponse>(_IssueEventResponse2IssueEventResult_QNAME, IssueEventResponse.class, IssueEventResponse2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageEventRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "request", scope = ManageEvent.class)
    public JAXBElement<ManageEventRequest> createManageEventRequest(ManageEventRequest value) {
        return new JAXBElement<ManageEventRequest>(_ExecuteBatchSaveRequest_QNAME, ManageEventRequest.class, ManageEvent.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageEventResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ManageEventResult", scope = ManageEventResponse2 .class)
    public JAXBElement<ManageEventResponse> createManageEventResponse2ManageEventResult(ManageEventResponse value) {
        return new JAXBElement<ManageEventResponse>(_ManageEventResponse2ManageEventResult_QNAME, ManageEventResponse.class, ManageEventResponse2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "AccountID", scope = ServicePoint.class)
    public JAXBElement<String> createServicePointAccountID(String value) {
        return new JAXBElement<String>(_ServicePointAccountID_QNAME, String.class, ServicePoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "AccountNumber", scope = ServicePoint.class)
    public JAXBElement<String> createServicePointAccountNumber(String value) {
        return new JAXBElement<String>(_ServicePointAccountNumber_QNAME, String.class, ServicePoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ServicePointID", scope = ServicePoint.class)
    public JAXBElement<String> createServicePointServicePointID(String value) {
        return new JAXBElement<String>(_ServicePointServicePointID_QNAME, String.class, ServicePoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "CallbackAddress", scope = ManageEventRequest.class)
    public JAXBElement<String> createManageEventRequestCallbackAddress(String value) {
        return new JAXBElement<String>(_ManageEventRequestCallbackAddress_QNAME, String.class, ManageEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "EndDateTime", scope = ManageEventRequest.class)
    public JAXBElement<XMLGregorianCalendar> createManageEventRequestEndDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ManageEventRequestEndDateTime_QNAME, XMLGregorianCalendar.class, ManageEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "EventAlternateID", scope = ManageEventRequest.class)
    public JAXBElement<String> createManageEventRequestEventAlternateID(String value) {
        return new JAXBElement<String>(_ManageEventRequestEventAlternateID_QNAME, String.class, ManageEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "EventID", scope = ManageEventRequest.class)
    public JAXBElement<Integer> createManageEventRequestEventID(Integer value) {
        return new JAXBElement<Integer>(_ManageEventRequestEventID_QNAME, Integer.class, ManageEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "RequestingSystem", scope = ManageEventRequest.class)
    public JAXBElement<String> createManageEventRequestRequestingSystem(String value) {
        return new JAXBElement<String>(_ManageEventRequestRequestingSystem_QNAME, String.class, ManageEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "StartDateTime", scope = ManageEventRequest.class)
    public JAXBElement<XMLGregorianCalendar> createManageEventRequestStartDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ManageEventRequestStartDateTime_QNAME, XMLGregorianCalendar.class, ManageEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "TerminationDateTime", scope = ManageEventRequest.class)
    public JAXBElement<XMLGregorianCalendar> createManageEventRequestTerminationDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ManageEventRequestTerminationDateTime_QNAME, XMLGregorianCalendar.class, ManageEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "AdjustmentFactor", scope = IssueEventServicePoint.class)
    public JAXBElement<Double> createIssueEventServicePointAdjustmentFactor(Double value) {
        return new JAXBElement<Double>(_IssueEventServicePointAdjustmentFactor_QNAME, Double.class, IssueEventServicePoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "CLPMonthly", scope = IssueEventServicePoint.class)
    public JAXBElement<Double> createIssueEventServicePointCLPMonthly(Double value) {
        return new JAXBElement<Double>(_IssueEventServicePointCLPMonthly_QNAME, Double.class, IssueEventServicePoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ID", scope = IssueEventServicePoint.class)
    public JAXBElement<String> createIssueEventServicePointID(String value) {
        return new JAXBElement<String>(_IssueEventServicePointID_QNAME, String.class, IssueEventServicePoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "MaximumReduction", scope = IssueEventServicePoint.class)
    public JAXBElement<Double> createIssueEventServicePointMaximumReduction(Double value) {
        return new JAXBElement<Double>(_IssueEventServicePointMaximumReduction_QNAME, Double.class, IssueEventServicePoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "Message", scope = IssueEventServicePoint.class)
    public JAXBElement<String> createIssueEventServicePointMessage(String value) {
        return new JAXBElement<String>(_IssueEventServicePointMessage_QNAME, String.class, IssueEventServicePoint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "AdditionalReductionPrice", scope = IssueEventBlock.class)
    public JAXBElement<Double> createIssueEventBlockAdditionalReductionPrice(Double value) {
        return new JAXBElement<Double>(_IssueEventBlockAdditionalReductionPrice_QNAME, Double.class, IssueEventBlock.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "BlockPrice", scope = IssueEventBlock.class)
    public JAXBElement<Double> createIssueEventBlockBlockPrice(Double value) {
        return new JAXBElement<Double>(_IssueEventBlockBlockPrice_QNAME, Double.class, IssueEventBlock.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "PenaltyPrice", scope = IssueEventBlock.class)
    public JAXBElement<Double> createIssueEventBlockPenaltyPrice(Double value) {
        return new JAXBElement<Double>(_IssueEventBlockPenaltyPrice_QNAME, Double.class, IssueEventBlock.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "AlternateID", scope = IssueEventResponse.class)
    public JAXBElement<String> createIssueEventResponseAlternateID(String value) {
        return new JAXBElement<String>(_IssueEventResponseAlternateID_QNAME, String.class, IssueEventResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ContinueOnServicePointValidationFailure", scope = IssueEventRequest.class)
    public JAXBElement<Boolean> createIssueEventRequestContinueOnServicePointValidationFailure(Boolean value) {
        return new JAXBElement<Boolean>(_IssueEventRequestContinueOnServicePointValidationFailure_QNAME, Boolean.class, IssueEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "DelayNoticeUntillDateTime", scope = IssueEventRequest.class)
    public JAXBElement<XMLGregorianCalendar> createIssueEventRequestDelayNoticeUntillDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_IssueEventRequestDelayNoticeUntillDateTime_QNAME, XMLGregorianCalendar.class, IssueEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "DemandCapValue", scope = IssueEventRequest.class)
    public JAXBElement<Double> createIssueEventRequestDemandCapValue(Double value) {
        return new JAXBElement<Double>(_IssueEventRequestDemandCapValue_QNAME, Double.class, IssueEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "EventAlternateID", scope = IssueEventRequest.class)
    public JAXBElement<String> createIssueEventRequestEventAlternateID(String value) {
        return new JAXBElement<String>(_ManageEventRequestEventAlternateID_QNAME, String.class, IssueEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IncludeServicePointsInOverlappingEvents", scope = IssueEventRequest.class)
    public JAXBElement<Boolean> createIssueEventRequestIncludeServicePointsInOverlappingEvents(Boolean value) {
        return new JAXBElement<Boolean>(_IssueEventRequestIncludeServicePointsInOverlappingEvents_QNAME, Boolean.class, IssueEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IsTestEvent", scope = IssueEventRequest.class)
    public JAXBElement<Boolean> createIssueEventRequestIsTestEvent(Boolean value) {
        return new JAXBElement<Boolean>(_IssueEventRequestIsTestEvent_QNAME, Boolean.class, IssueEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "Message", scope = IssueEventRequest.class)
    public JAXBElement<String> createIssueEventRequestMessage(String value) {
        return new JAXBElement<String>(_IssueEventServicePointMessage_QNAME, String.class, IssueEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "RespondByDateTime", scope = IssueEventRequest.class)
    public JAXBElement<XMLGregorianCalendar> createIssueEventRequestRespondByDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_IssueEventRequestRespondByDateTime_QNAME, XMLGregorianCalendar.class, IssueEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "StartDialingMetersDateTime", scope = IssueEventRequest.class)
    public JAXBElement<XMLGregorianCalendar> createIssueEventRequestStartDialingMetersDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_IssueEventRequestStartDialingMetersDateTime_QNAME, XMLGregorianCalendar.class, IssueEventRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIssueEventResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IssueEvent_Responses", scope = CurtailmentEventSaveBatchResponse.class)
    public JAXBElement<ArrayOfIssueEventResponse> createCurtailmentEventSaveBatchResponseIssueEventResponses(ArrayOfIssueEventResponse value) {
        return new JAXBElement<ArrayOfIssueEventResponse>(_CurtailmentEventSaveBatchResponseIssueEventResponses_QNAME, ArrayOfIssueEventResponse.class, CurtailmentEventSaveBatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfManageEventResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ManageEvent_Responses", scope = CurtailmentEventSaveBatchResponse.class)
    public JAXBElement<ArrayOfManageEventResponse> createCurtailmentEventSaveBatchResponseManageEventResponses(ArrayOfManageEventResponse value) {
        return new JAXBElement<ArrayOfManageEventResponse>(_CurtailmentEventSaveBatchResponseManageEventResponses_QNAME, ArrayOfManageEventResponse.class, CurtailmentEventSaveBatchResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfIssueEventRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "IssueEvent_Requests", scope = CurtailmentEventSaveBatchRequest.class)
    public JAXBElement<ArrayOfIssueEventRequest> createCurtailmentEventSaveBatchRequestIssueEventRequests(ArrayOfIssueEventRequest value) {
        return new JAXBElement<ArrayOfIssueEventRequest>(_CurtailmentEventSaveBatchRequestIssueEventRequests_QNAME, ArrayOfIssueEventRequest.class, CurtailmentEventSaveBatchRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfManageEventRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.itron.com/mdm/curtailment/2008/05", name = "ManageEvent_Requests", scope = CurtailmentEventSaveBatchRequest.class)
    public JAXBElement<ArrayOfManageEventRequest> createCurtailmentEventSaveBatchRequestManageEventRequests(ArrayOfManageEventRequest value) {
        return new JAXBElement<ArrayOfManageEventRequest>(_CurtailmentEventSaveBatchRequestManageEventRequests_QNAME, ArrayOfManageEventRequest.class, CurtailmentEventSaveBatchRequest.class, value);
    }

}
