package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="BASE_SERVICE_POINT")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseServicePoint implements Serializable, VModelEntity{
    private static final long serialVersionUID = 1L;
    private String servicePointId;
    private String naics;
    private String operationArea;
    private String customerMdmaCompanyName;
    private String customerMspCompanyName;
    private String meterReadCycle12;
    private String serviceType;
    private String latitude;
    private String longitude;
    private String robCode;
    private String customerMdmaCode;
    private String customerMspCode;
    private Premise premise;
    private BaseMeter meter;
    private BigDecimal maxDemand;

    public BaseServicePoint() {
    }

    @Id
    @Column(name = "SERVICE_POINT_ID")
    public String getServicePointId() {
        return this.servicePointId;
    }

    public void setServicePointId(String servicePointId) {
        this.servicePointId = servicePointId;
    }

    @Column(name = "NAICS")
    public String getNaics() {
        return this.naics;
    }

    public void setNaics(String naics) {
        this.naics = naics;
    }

    @Column(name = "OPERATION_AREA")
    public String getOperationArea() {
        return operationArea;
    }


    public void setOperationArea(String operationArea) {
        this.operationArea = operationArea;
    }

    @Column(name = "CUSTOMER_MDMA_COMPANY_NAME")
    public String getCustomerMdmaCompanyName() {
        return customerMdmaCompanyName;
    }


    public void setCustomerMdmaCompanyName(String customerMdmaCompanyName) {
        this.customerMdmaCompanyName = customerMdmaCompanyName;
    }

    @Column(name = "CUSTOMER_MSP_COMPANY_NAME")
    public String getCustomerMspCompanyName() {
        return customerMspCompanyName;
    }


    public void setCustomerMspCompanyName(String customerMspCompanyName) {
        this.customerMspCompanyName = customerMspCompanyName;
    }

    @Column(name = "CUST_METER_READ_CYCLE12")
    public String getMeterReadCycle12() {
        return meterReadCycle12;
    }


    public void setMeterReadCycle12(String meterReadCycle12) {
        this.meterReadCycle12 = meterReadCycle12;
    }

    @Column(name = "SERVICE_TYPE")
    public String getServiceType() {
        return serviceType;
    }


    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Column(name = "LATITUDE")
    public String getLatitude() {
        return latitude;
    }


    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Column(name = "LONGITUDE")
    public String getLongitude() {
        return longitude;
    }


    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Column(name = "ROB_CODE")
    public String getRobCode() {
        return robCode;
    }


    public void setRobCode(String robCode) {
        this.robCode = robCode;
    }

    @Column(name = "CUSTOMER_MDMA_CODE")
    public String getCustomerMdmaCode() {
        return customerMdmaCode;
    }

    public void setCustomerMdmaCode(String customerMdmaCode) {
        this.customerMdmaCode = customerMdmaCode;
    }

    @Column(name = "CUSTOMER_MSP_CODE")
    public String getCustomerMspCode() {
        return customerMspCode;
    }

    public void setCustomerMspCode(String customerMspCode) {
        this.customerMspCode = customerMspCode;
    }

    //bi-directional many-to-one association to Premise
    @ManyToOne
    @JoinColumn(name = "PREMISE_ID")
    @JsonManagedReference
    public Premise getPremise() {
        return this.premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    //bi-directional many-to-one association to Meter
    @ManyToOne
    @JoinColumn(name = "METER_ID")
    @JsonManagedReference
    public BaseMeter getMeter() {
        return this.meter;
    }

    public void setMeter(BaseMeter meter) {
        this.meter = meter;
    }

    @Column(name = "MAX_DEMAND")
    public BigDecimal getMaxDemand() {
        return maxDemand;
    }

    public void setMaxDemand(BigDecimal maxDemand) {
        this.maxDemand = maxDemand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseServicePoint that = (BaseServicePoint) o;

        if (servicePointId != null ? !servicePointId.equals(that.servicePointId) : that.servicePointId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return servicePointId != null ? servicePointId.hashCode() : 0;
    }

    public String idFieldName() {
        return "servicePointId";
    }

    public List<String> relationshipFieldNames() {
        return Arrays.asList("premise", "meter");
    }

    public List<String> excludedFieldsToCompare() {
        return null;
    }
}
