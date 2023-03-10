
package com.caiso.soa.batchvalidationstatus_v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.caiso.soa.batchvalidationstatus_v1 package. 
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

    private final static QName _BatchValidationStatus_QNAME = new QName("http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#", "BatchValidationStatus");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.caiso.soa.batchvalidationstatus_v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BatchValidationStatus }
     * 
     */
    public BatchValidationStatus createBatchValidationStatus() {
        return new BatchValidationStatus();
    }

    /**
     * Create an instance of {@link MessageHeader }
     * 
     */
    public MessageHeader createMessageHeader() {
        return new MessageHeader();
    }

    /**
     * Create an instance of {@link MessagePayload }
     * 
     */
    public MessagePayload createMessagePayload() {
        return new MessagePayload();
    }

    /**
     * Create an instance of {@link BatchStatus }
     * 
     */
    public BatchStatus createBatchStatus() {
        return new BatchStatus();
    }

    /**
     * Create an instance of {@link DemandResponseRegistration }
     * 
     */
    public DemandResponseRegistration createDemandResponseRegistration() {
        return new DemandResponseRegistration();
    }

    /**
     * Create an instance of {@link DistributedEnergyResourceContainer }
     * 
     */
    public DistributedEnergyResourceContainer createDistributedEnergyResourceContainer() {
        return new DistributedEnergyResourceContainer();
    }

    /**
     * Create an instance of {@link ErrorLog }
     * 
     */
    public ErrorLog createErrorLog() {
        return new ErrorLog();
    }

    /**
     * Create an instance of {@link Flowgate }
     * 
     */
    public Flowgate createFlowgate() {
        return new Flowgate();
    }

    /**
     * Create an instance of {@link IdentifiedObjectBatch }
     * 
     */
    public IdentifiedObjectBatch createIdentifiedObjectBatch() {
        return new IdentifiedObjectBatch();
    }

    /**
     * Create an instance of {@link IdentifiedObjectMridName }
     * 
     */
    public IdentifiedObjectMridName createIdentifiedObjectMridName() {
        return new IdentifiedObjectMridName();
    }

    /**
     * Create an instance of {@link IdentifiedObjectOptMrid }
     * 
     */
    public IdentifiedObjectOptMrid createIdentifiedObjectOptMrid() {
        return new IdentifiedObjectOptMrid();
    }

    /**
     * Create an instance of {@link Measurement }
     * 
     */
    public Measurement createMeasurement() {
        return new Measurement();
    }

    /**
     * Create an instance of {@link MeasurementValue }
     * 
     */
    public MeasurementValue createMeasurementValue() {
        return new MeasurementValue();
    }

    /**
     * Create an instance of {@link PowerSystemResource }
     * 
     */
    public PowerSystemResource createPowerSystemResource() {
        return new PowerSystemResource();
    }

    /**
     * Create an instance of {@link RegisteredGenerator }
     * 
     */
    public RegisteredGenerator createRegisteredGenerator() {
        return new RegisteredGenerator();
    }

    /**
     * Create an instance of {@link RegisteredInterTie }
     * 
     */
    public RegisteredInterTie createRegisteredInterTie() {
        return new RegisteredInterTie();
    }

    /**
     * Create an instance of {@link RegisteredLoad }
     * 
     */
    public RegisteredLoad createRegisteredLoad() {
        return new RegisteredLoad();
    }

    /**
     * Create an instance of {@link RegisteredResource }
     * 
     */
    public RegisteredResource createRegisteredResource() {
        return new RegisteredResource();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BatchValidationStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.caiso.com/soa/BatchValidationStatus_v1.xsd#", name = "BatchValidationStatus")
    public JAXBElement<BatchValidationStatus> createBatchValidationStatus(BatchValidationStatus value) {
        return new JAXBElement<BatchValidationStatus>(_BatchValidationStatus_QNAME, BatchValidationStatus.class, null, value);
    }

}
