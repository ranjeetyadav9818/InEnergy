
package com.caiso.ads.api.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.caiso.ads.api.model.trajectory package. 
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
public class ObjectFactoryTrajectory {

    private final static QName _APITrajectoryResponse_QNAME = new QName("http://ads.caiso.com", "APITrajectoryResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.caiso.ads.api.model.trajectory
     * 
     */
    public ObjectFactoryTrajectory() {
    }

    /**
     * Create an instance of {@link APITrajectoryResponseType }
     * 
     */
    public APITrajectoryResponseType createAPITrajectoryResponseType() {
        return new APITrajectoryResponseType();
    }

    /**
     * Create an instance of {@link DopListType }
     * 
     */
    public DopListType createDopListType() {
        return new DopListType();
    }

    /**
     * Create an instance of {@link TrajectoryBatchListType }
     * 
     */
    public TrajectoryBatchListType createTrajectoryBatchListType() {
        return new TrajectoryBatchListType();
    }

    /**
     * Create an instance of {@link TrajectoryDopType }
     * 
     */
    public TrajectoryDopType createTrajectoryDopType() {
        return new TrajectoryDopType();
    }

    /**
     * Create an instance of {@link ComplianceListType }
     * 
     */
    public ComplianceListType createComplianceListType() {
        return new ComplianceListType();
    }

    /**
     * Create an instance of {@link TrajectoryComplianceType }
     * 
     */
    public TrajectoryComplianceType createTrajectoryComplianceType() {
        return new TrajectoryComplianceType();
    }

    /**
     * Create an instance of {@link TrajectoryBatchType }
     * 
     */
    public TrajectoryBatchType createTrajectoryBatchType() {
        return new TrajectoryBatchType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link APITrajectoryResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ads.caiso.com", name = "APITrajectoryResponse")
    public JAXBElement<APITrajectoryResponseType> createAPITrajectoryResponse(APITrajectoryResponseType value) {
        return new JAXBElement<APITrajectoryResponseType>(_APITrajectoryResponse_QNAME, APITrajectoryResponseType.class, null, value);
    }

}
