
package com.caiso.soa._2006_06_13.standardattachmentinfor;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.caiso.soa._2006_06_13.standardattachmentinfor package. 
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

    private final static QName _StandardAttachmentInfor_QNAME = new QName("http://www.caiso.com/soa/2006-06-13/StandardAttachmentInfor.xsd", "standardAttachmentInfor");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.caiso.soa._2006_06_13.standardattachmentinfor
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StandardAttachmentInfor }
     * 
     */
    public StandardAttachmentInfor createStandardAttachmentInfor() {
        return new StandardAttachmentInfor();
    }

    /**
     * Create an instance of {@link Attachment }
     * 
     */
    public Attachment createAttachment() {
        return new Attachment();
    }

    /**
     * Create an instance of {@link AttributeList }
     * 
     */
    public AttributeList createAttributeList() {
        return new AttributeList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StandardAttachmentInfor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.caiso.com/soa/2006-06-13/StandardAttachmentInfor.xsd", name = "standardAttachmentInfor")
    public JAXBElement<StandardAttachmentInfor> createStandardAttachmentInfor(StandardAttachmentInfor value) {
        return new JAXBElement<StandardAttachmentInfor>(_StandardAttachmentInfor_QNAME, StandardAttachmentInfor.class, null, value);
    }

}
