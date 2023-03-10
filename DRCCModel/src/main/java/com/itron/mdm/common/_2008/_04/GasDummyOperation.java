
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
public class GasDummyOperation {

    @XmlElementRef(name = "request", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasRequestBase> request;
    @XmlElementRef(name = "response", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasResponseBase> response;
    @XmlElementRef(name = "transfer", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasTransferBase> transfer;
    @XmlElementRef(name = "batchRequest", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasBatchGasRequestBase> batchRequest;
    @XmlElementRef(name = "batchTransfer", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasBatchGasTransferBase> batchTransfer;
    @XmlElementRef(name = "subRequest", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasSubRequestBase> subRequest;
    @XmlElementRef(name = "batchResponse", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasBatchGasResponseBase> batchResponse;
    @XmlElementRef(name = "statisticsSummary", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasStatisticsSummary> statisticsSummary;
    @XmlElementRef(name = "statisticsSummaryCollection", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasStatisticsSummaryCollection> statisticsSummaryCollection;
    @XmlElementRef(name = "reportPaging", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasReportPaging> reportPaging;
    @XmlSchemaType(name = "string")
    protected GasFileInventoryFileType fileType;
    @XmlSchemaType(name = "string")
    protected GasFileInventoryFileState fileState;
    @XmlElementRef(name = "sortElement", namespace = "http://www.itron.com/mdm/common/2008/04", type = JAXBElement.class, required = false)
    protected JAXBElement<GasSortElement> sortElement;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasRequestBase }{@code >}
     *     
     */
    public JAXBElement<GasRequestBase> getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasRequestBase }{@code >}
     *     
     */
    public void setRequest(JAXBElement<GasRequestBase> value) {
        this.request = value;
    }

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasResponseBase }{@code >}
     *     
     */
    public JAXBElement<GasResponseBase> getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasResponseBase }{@code >}
     *     
     */
    public void setResponse(JAXBElement<GasResponseBase> value) {
        this.response = value;
    }

    /**
     * Gets the value of the transfer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasTransferBase }{@code >}
     *     
     */
    public JAXBElement<GasTransferBase> getTransfer() {
        return transfer;
    }

    /**
     * Sets the value of the transfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasTransferBase }{@code >}
     *     
     */
    public void setTransfer(JAXBElement<GasTransferBase> value) {
        this.transfer = value;
    }

    /**
     * Gets the value of the batchRequest property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasBatchGasRequestBase }{@code >}
     *     
     */
    public JAXBElement<GasBatchGasRequestBase> getBatchRequest() {
        return batchRequest;
    }

    /**
     * Sets the value of the batchRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasBatchGasRequestBase }{@code >}
     *     
     */
    public void setBatchRequest(JAXBElement<GasBatchGasRequestBase> value) {
        this.batchRequest = value;
    }

    /**
     * Gets the value of the batchTransfer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasBatchGasTransferBase }{@code >}
     *     
     */
    public JAXBElement<GasBatchGasTransferBase> getBatchTransfer() {
        return batchTransfer;
    }

    /**
     * Sets the value of the batchTransfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasBatchGasTransferBase }{@code >}
     *     
     */
    public void setBatchTransfer(JAXBElement<GasBatchGasTransferBase> value) {
        this.batchTransfer = value;
    }

    /**
     * Gets the value of the subRequest property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasSubRequestBase }{@code >}
     *     
     */
    public JAXBElement<GasSubRequestBase> getSubRequest() {
        return subRequest;
    }

    /**
     * Sets the value of the subRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasSubRequestBase }{@code >}
     *     
     */
    public void setSubRequest(JAXBElement<GasSubRequestBase> value) {
        this.subRequest = value;
    }

    /**
     * Gets the value of the batchResponse property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasBatchGasResponseBase }{@code >}
     *     
     */
    public JAXBElement<GasBatchGasResponseBase> getBatchResponse() {
        return batchResponse;
    }

    /**
     * Sets the value of the batchResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasBatchGasResponseBase }{@code >}
     *     
     */
    public void setBatchResponse(JAXBElement<GasBatchGasResponseBase> value) {
        this.batchResponse = value;
    }

    /**
     * Gets the value of the statisticsSummary property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasStatisticsSummary }{@code >}
     *     
     */
    public JAXBElement<GasStatisticsSummary> getStatisticsSummary() {
        return statisticsSummary;
    }

    /**
     * Sets the value of the statisticsSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasStatisticsSummary }{@code >}
     *     
     */
    public void setStatisticsSummary(JAXBElement<GasStatisticsSummary> value) {
        this.statisticsSummary = value;
    }

    /**
     * Gets the value of the statisticsSummaryCollection property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasStatisticsSummaryCollection }{@code >}
     *     
     */
    public JAXBElement<GasStatisticsSummaryCollection> getStatisticsSummaryCollection() {
        return statisticsSummaryCollection;
    }

    /**
     * Sets the value of the statisticsSummaryCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasStatisticsSummaryCollection }{@code >}
     *     
     */
    public void setStatisticsSummaryCollection(JAXBElement<GasStatisticsSummaryCollection> value) {
        this.statisticsSummaryCollection = value;
    }

    /**
     * Gets the value of the reportPaging property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasReportPaging }{@code >}
     *     
     */
    public JAXBElement<GasReportPaging> getReportPaging() {
        return reportPaging;
    }

    /**
     * Sets the value of the reportPaging property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasReportPaging }{@code >}
     *     
     */
    public void setReportPaging(JAXBElement<GasReportPaging> value) {
        this.reportPaging = value;
    }

    /**
     * Gets the value of the fileType property.
     * 
     * @return
     *     possible object is
     *     {@link GasFileInventoryFileType }
     *     
     */
    public GasFileInventoryFileType getFileType() {
        return fileType;
    }

    /**
     * Sets the value of the fileType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GasFileInventoryFileType }
     *     
     */
    public void setFileType(GasFileInventoryFileType value) {
        this.fileType = value;
    }

    /**
     * Gets the value of the fileState property.
     * 
     * @return
     *     possible object is
     *     {@link GasFileInventoryFileState }
     *     
     */
    public GasFileInventoryFileState getFileState() {
        return fileState;
    }

    /**
     * Sets the value of the fileState property.
     * 
     * @param value
     *     allowed object is
     *     {@link GasFileInventoryFileState }
     *     
     */
    public void setFileState(GasFileInventoryFileState value) {
        this.fileState = value;
    }

    /**
     * Gets the value of the sortElement property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GasSortElement }{@code >}
     *     
     */
    public JAXBElement<GasSortElement> getSortElement() {
        return sortElement;
    }

    /**
     * Sets the value of the sortElement property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GasSortElement }{@code >}
     *     
     */
    public void setSortElement(JAXBElement<GasSortElement> value) {
        this.sortElement = value;
    }

}
