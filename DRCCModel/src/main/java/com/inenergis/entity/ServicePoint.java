package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


/**
 * The persistent class for the SERVICE_POINT database table.
 */
@Entity
@Table(name = "SERVICE_POINT")
@NamedQuery(name = "ServicePoint.findAll", query = "SELECT s FROM ServicePoint s")
//@PrimaryKeyJoinColumn(name="id")
public class ServicePoint extends BaseServicePoint implements Serializable, VModelEntity {
    private static final long serialVersionUID = 1L;
    private String elecUsageNonres;
    private String feeder;
    private String substation;
    private String serviceVoltageClass;
    private String circuitNumber;
    private String subStationNumber;
    private String sourceSideDeviceNumber;
    private String trfmrNumber;
    private String trfmrBadgeNumber;

    public ServicePoint() {
    }

//    @Id
//    @Column(name = "SERVICE_POINT_ID")
//    public String getServicePointId() {
//        return this.servicePointId;
//    }
//
//    public void setServicePointId(String servicePointId) {
//        this.servicePointId = servicePointId;
//    }


    @Column(name = "ELEC_USAGE_NONRES")
    public String getElecUsageNonres() {
        return this.elecUsageNonres;
    }

    public void setElecUsageNonres(String elecUsageNonres) {
        this.elecUsageNonres = elecUsageNonres;
    }

    @Column(name = "FEEDER")
    public String getFeeder() {
        return this.feeder;
    }

    public void setFeeder(String feeder) {
        this.feeder = feeder;
    }


    @Column(name = "SUBSTATION")
    public String getSubstation() {
        return this.substation;
    }

    public void setSubstation(String substation) {
        this.substation = substation;
    }

    @Column(name = "CUST_SERVICE_VOLTAGE_CLASS")
    public String getServiceVoltageClass() {
        return serviceVoltageClass;
    }


    public void setServiceVoltageClass(String serviceVoltageClass) {
        this.serviceVoltageClass = serviceVoltageClass;
    }


    @Column(name = "CIRCUIT_NUMBER")
    public String getCircuitNumber() {
        return circuitNumber;
    }


    public void setCircuitNumber(String circuitNumber) {
        this.circuitNumber = circuitNumber;
    }

    @Column(name = "SUB_STATION_NUMBER")
    public String getSubStationNumber() {
        return subStationNumber;
    }


    public void setSubStationNumber(String subStationNumber) {
        this.subStationNumber = subStationNumber;
    }

    @Column(name = "SOURCE_SIDE_DEVICE_NUMBER")
    public String getSourceSideDeviceNumber() {
        return sourceSideDeviceNumber;
    }


    public void setSourceSideDeviceNumber(String sourceSideDeviceNumber) {
        this.sourceSideDeviceNumber = sourceSideDeviceNumber;
    }

    @Column(name = "TRFMR_NUMBER")
    public String getTrfmrNumber() {
        return trfmrNumber;
    }


    public void setTrfmrNumber(String trfmrNumber) {
        this.trfmrNumber = trfmrNumber;
    }

    @Column(name = "TRFMR_BDG_NUMBER")
    public String getTrfmrBadgeNumber() {
        return trfmrBadgeNumber;
    }


    public void setTrfmrBadgeNumber(String trfmrBadgeNumber) {
        this.trfmrBadgeNumber = trfmrBadgeNumber;
    }


}