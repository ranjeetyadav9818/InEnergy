
package com.caiso.ads.api.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.caiso.ads.api.model package. 
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

    private final static QName _APIDispatchResponse_QNAME = new QName("http://ads.caiso.com", "APIDispatchResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.caiso.ads.api.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link APIDispatchResponseType }
     * 
     */
    public APIDispatchResponseType createAPIDispatchResponseType() {
        return new APIDispatchResponseType();
    }

    /**
     * Create an instance of {@link DetailType }
     * 
     */
    public DetailType createDetailType() {
        return new DetailType();
    }

    /**
     * Create an instance of {@link InstructionDetailType }
     * 
     */
    public InstructionDetailType createInstructionDetailType() {
        return new InstructionDetailType();
    }

    /**
     * Create an instance of {@link InstructionType }
     * 
     */
    public InstructionType createInstructionType() {
        return new InstructionType();
    }

    /**
     * Create an instance of {@link InstructionsType }
     * 
     */
    public InstructionsType createInstructionsType() {
        return new InstructionsType();
    }

    /**
     * Create an instance of {@link DispatchBatchType }
     * 
     */
    public DispatchBatchType createDispatchBatchType() {
        return new DispatchBatchType();
    }

    /**
     * Create an instance of {@link DispatchBatchListType }
     * 
     */
    public DispatchBatchListType createDispatchBatchListType() {
        return new DispatchBatchListType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link APIDispatchResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ads.caiso.com", name = "APIDispatchResponse")
    public JAXBElement<APIDispatchResponseType> createAPIDispatchResponse(APIDispatchResponseType value) {
        return new JAXBElement<APIDispatchResponseType>(_APIDispatchResponse_QNAME, APIDispatchResponseType.class, null, value);
    }

}
