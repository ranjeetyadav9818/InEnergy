
package com.caiso.soa.standardoutput_v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.caiso.soa.standardoutput_v1 package. 
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

    private final static QName _StandardOutput_QNAME = new QName("http://www.caiso.com/soa/StandardOutput_v1.xsd#", "StandardOutput");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.caiso.soa.standardoutput_v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StandardOutput }
     * 
     */
    public StandardOutput createStandardOutput() {
        return new StandardOutput();
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
     * Create an instance of {@link Batch }
     * 
     */
    public Batch createBatch() {
        return new Batch();
    }

    /**
     * Create an instance of {@link Event }
     * 
     */
    public Event createEvent() {
        return new Event();
    }

    /**
     * Create an instance of {@link EventLog }
     * 
     */
    public EventLog createEventLog() {
        return new EventLog();
    }

    /**
     * Create an instance of {@link IdentifiedObject }
     * 
     */
    public IdentifiedObject createIdentifiedObject() {
        return new IdentifiedObject();
    }

    /**
     * Create an instance of {@link Service }
     * 
     */
    public Service createService() {
        return new Service();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StandardOutput }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.caiso.com/soa/StandardOutput_v1.xsd#", name = "StandardOutput")
    public JAXBElement<StandardOutput> createStandardOutput(StandardOutput value) {
        return new JAXBElement<StandardOutput>(_StandardOutput_QNAME, StandardOutput.class, null, value);
    }

}
