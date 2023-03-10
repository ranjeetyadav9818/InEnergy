
package com.itron.mdm.common._2008._04;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="request" type="{http://www.itron.com/mdm/common/2008/04}RequestBase" minOccurs="0"/&gt;
 *         &lt;element name="response" type="{http://www.itron.com/mdm/common/2008/04}ResponseBase" minOccurs="0"/&gt;
 *         &lt;element name="transfer" type="{http://www.itron.com/mdm/common/2008/04}TransferBase" minOccurs="0"/&gt;
 *         &lt;element name="batchRequest" type="{http://www.itron.com/mdm/common/2008/04}BatchRequestBase" minOccurs="0"/&gt;
 *         &lt;element name="batchTransfer" type="{http://www.itron.com/mdm/common/2008/04}BatchTransferBase" minOccurs="0"/&gt;
 *         &lt;element name="subRequest" type="{http://www.itron.com/mdm/common/2008/04}SubRequestBase" minOccurs="0"/&gt;
 *         &lt;element name="batchResponse" type="{http://www.itron.com/mdm/common/2008/04}BatchResponseBase" minOccurs="0"/&gt;
 *         &lt;element name="statisticsSummary" type="{http://www.itron.com/mdm/common/2008/04}StatisticsSummary" minOccurs="0"/&gt;
 *         &lt;element name="statisticsSummaryCollection" type="{http://www.itron.com/mdm/common/2008/04}StatisticsSummaryCollection" minOccurs="0"/&gt;
 *         &lt;element name="reportPaging" type="{http://www.itron.com/mdm/common/2008/04}ReportPaging" minOccurs="0"/&gt;
 *         &lt;element name="fileType" type="{http://www.itron.com/mdm/common/2008/04}FileInventoryFileType" minOccurs="0"/&gt;
 *         &lt;element name="fileState" type="{http://www.itron.com/mdm/common/2008/04}FileInventoryFileState" minOccurs="0"/&gt;
 *         &lt;element name="sortElement" type="{http://www.itron.com/mdm/common/2008/04}SortElement" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "request",
    "response",
    "transfer",
    "batchRequest",
    "batchTransfer",
    "subRequest",
    "batchResponse",
    "statisticsSummary",
    "statisticsSummaryCollection",
    "reportPaging",
    "fileType",
    "fileState",
    "sortElement"
})
@XmlRootElement(name = "DummyOperation")
public class DummyOperation {

    @XmlElementRef(name = "request", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<RequestBase> request;
    @XmlElementRef(name = "response", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<ResponseBase> response;
    @XmlElementRef(name = "transfer", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<TransferBase> transfer;
    @XmlElementRef(name = "batchRequest", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<BatchRequestBase> batchRequest;
    @XmlElementRef(name = "batchTransfer", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<BatchTransferBase> batchTransfer;
    @XmlElementRef(name = "subRequest", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<SubRequestBase> subRequest;
    @XmlElementRef(name = "batchResponse", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<BatchResponseBase> batchResponse;
    @XmlElementRef(name = "statisticsSummary", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<StatisticsSummary> statisticsSummary;
    @XmlElementRef(name = "statisticsSummaryCollection", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<StatisticsSummaryCollection> statisticsSummaryCollection;
    @XmlElementRef(name = "reportPaging", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<ReportPaging> reportPaging;
    @XmlSchemaType(name = "string")
    protected FileInventoryFileType fileType;
    @XmlSchemaType(name = "string")
    protected FileInventoryFileState fileState;
    @XmlElementRef(name = "sortElement", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<SortElement> sortElement;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RequestBase }{@code >}
     *     
     */
    public JAXBElement<RequestBase> getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RequestBase }{@code >}
     *     
     */
    public void setRequest(JAXBElement<RequestBase> value) {
        this.request = value;
    }

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ResponseBase }{@code >}
     *     
     */
    public JAXBElement<ResponseBase> getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ResponseBase }{@code >}
     *     
     */
    public void setResponse(JAXBElement<ResponseBase> value) {
        this.response = value;
    }

    /**
     * Gets the value of the transfer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link TransferBase }{@code >}
     *     
     */
    public JAXBElement<TransferBase> getTransfer() {
        return transfer;
    }

    /**
     * Sets the value of the transfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link TransferBase }{@code >}
     *     
     */
    public void setTransfer(JAXBElement<TransferBase> value) {
        this.transfer = value;
    }

    /**
     * Gets the value of the batchRequest property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BatchRequestBase }{@code >}
     *     
     */
    public JAXBElement<BatchRequestBase> getBatchRequest() {
        return batchRequest;
    }

    /**
     * Sets the value of the batchRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BatchRequestBase }{@code >}
     *     
     */
    public void setBatchRequest(JAXBElement<BatchRequestBase> value) {
        this.batchRequest = value;
    }

    /**
     * Gets the value of the batchTransfer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BatchTransferBase }{@code >}
     *     
     */
    public JAXBElement<BatchTransferBase> getBatchTransfer() {
        return batchTransfer;
    }

    /**
     * Sets the value of the batchTransfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BatchTransferBase }{@code >}
     *     
     */
    public void setBatchTransfer(JAXBElement<BatchTransferBase> value) {
        this.batchTransfer = value;
    }

    /**
     * Gets the value of the subRequest property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SubRequestBase }{@code >}
     *     
     */
    public JAXBElement<SubRequestBase> getSubRequest() {
        return subRequest;
    }

    /**
     * Sets the value of the subRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SubRequestBase }{@code >}
     *     
     */
    public void setSubRequest(JAXBElement<SubRequestBase> value) {
        this.subRequest = value;
    }

    /**
     * Gets the value of the batchResponse property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BatchResponseBase }{@code >}
     *     
     */
    public JAXBElement<BatchResponseBase> getBatchResponse() {
        return batchResponse;
    }

    /**
     * Sets the value of the batchResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BatchResponseBase }{@code >}
     *     
     */
    public void setBatchResponse(JAXBElement<BatchResponseBase> value) {
        this.batchResponse = value;
    }

    /**
     * Gets the value of the statisticsSummary property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link StatisticsSummary }{@code >}
     *     
     */
    public JAXBElement<StatisticsSummary> getStatisticsSummary() {
        return statisticsSummary;
    }

    /**
     * Sets the value of the statisticsSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link StatisticsSummary }{@code >}
     *     
     */
    public void setStatisticsSummary(JAXBElement<StatisticsSummary> value) {
        this.statisticsSummary = value;
    }

    /**
     * Gets the value of the statisticsSummaryCollection property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link StatisticsSummaryCollection }{@code >}
     *     
     */
    public JAXBElement<StatisticsSummaryCollection> getStatisticsSummaryCollection() {
        return statisticsSummaryCollection;
    }

    /**
     * Sets the value of the statisticsSummaryCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link StatisticsSummaryCollection }{@code >}
     *     
     */
    public void setStatisticsSummaryCollection(JAXBElement<StatisticsSummaryCollection> value) {
        this.statisticsSummaryCollection = value;
    }

    /**
     * Gets the value of the reportPaging property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ReportPaging }{@code >}
     *     
     */
    public JAXBElement<ReportPaging> getReportPaging() {
        return reportPaging;
    }

    /**
     * Sets the value of the reportPaging property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ReportPaging }{@code >}
     *     
     */
    public void setReportPaging(JAXBElement<ReportPaging> value) {
        this.reportPaging = value;
    }

    /**
     * Gets the value of the fileType property.
     * 
     * @return
     *     possible object is
     *     {@link FileInventoryFileType }
     *     
     */
    public FileInventoryFileType getFileType() {
        return fileType;
    }

    /**
     * Sets the value of the fileType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileInventoryFileType }
     *     
     */
    public void setFileType(FileInventoryFileType value) {
        this.fileType = value;
    }

    /**
     * Gets the value of the fileState property.
     * 
     * @return
     *     possible object is
     *     {@link FileInventoryFileState }
     *     
     */
    public FileInventoryFileState getFileState() {
        return fileState;
    }

    /**
     * Sets the value of the fileState property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileInventoryFileState }
     *     
     */
    public void setFileState(FileInventoryFileState value) {
        this.fileState = value;
    }

    /**
     * Gets the value of the sortElement property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SortElement }{@code >}
     *     
     */
    public JAXBElement<SortElement> getSortElement() {
        return sortElement;
    }

    /**
     * Sets the value of the sortElement property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SortElement }{@code >}
     *     
     */
    public void setSortElement(JAXBElement<SortElement> value) {
        this.sortElement = value;
    }

}
