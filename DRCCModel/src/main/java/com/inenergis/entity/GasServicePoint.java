package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


/**
 * The persistent class for the SERVICE_POINT database table.
 */
@Entity
@Table(name = "GAS_SERVICE_POINT")
@NamedQuery(name = "GasServicePoint.findAll", query = "SELECT s FROM GasServicePoint s")
public class GasServicePoint extends  BaseServicePoint  {
    private static final long serialVersionUID = 1L;
    private String feeder;
    private String cityGate;
    private String deliveredPressure;
    private String servicePressure;
    private String meterPressure;
    private String commsId;
    private String servicePressureClass;
    private String cityGateNumber;
    private String regulatorNumber;
    private String regulatorBadgeNumber;
//    private GasMeter meter;

    public GasServicePoint() {
    }


    @Column(name = "FEEDER")
    public String getFeeder() {
        return this.feeder;
    }

    public void setFeeder(String feeder) {
        this.feeder = feeder;
    }

    @Column(name = "CITY_GATE")
    public String getCityGate() {
        return this.cityGate;
    }

    public void setCityGate(String cityGate) {
        this.cityGate = cityGate;
    }


    @Column(name = "CUST_SERVICE_PRESSURE_CLASS")
    public String getServicePressureClass() {
        return servicePressureClass;
    }


    public void setServicePressureClass(String servicePressureClass) {
        this.servicePressureClass = servicePressureClass;
    }
    @Column(name = "DELIVERED_PRESSURE")
    public String getDeliveredPressure() {
        return deliveredPressure;
    }

    public void setDeliveredPressure(String deliveredPressure) {
        this.deliveredPressure = deliveredPressure;
    }
    @Column(name = "SERVICE_PRESSURE")
    public String getServicePressure() {
        return servicePressure;
    }

    public void setServicePressure(String servicePressure) {
        this.servicePressure = servicePressure;
    }
    @Column(name = "COMMS_ID")
    public String getCommsId() {
        return commsId;
    }

    public void setCommsId(String commsId) {
        this.commsId = commsId;
    }
    @Column(name = "METER_PRESSURE")
    public String getMeterPressure() {
        return meterPressure;
    }

    public void setMeterPressure(String meterPressure) {
        this.meterPressure = meterPressure;
    }

    @Column(name = "CITY_GATE_NUMBER")
    public String getCityGateNumber() {
        return cityGateNumber;
    }


    public void setCityGateNumber(String cityGateNumber) {
        this.cityGateNumber = cityGateNumber;
    }


    @Column(name = "REGULATOR_NUMBER")
    public String getRegulatorNumber() {
        return regulatorNumber;
    }


    public void setRegulatorNumber(String regulatorNumber) {
        this.regulatorNumber = regulatorNumber;
    }

    @Column(name = "REGULATOR_BDG_NUMBER")
    public String getRegulatorBadgeNumber() {
        return regulatorBadgeNumber;
    }


    public void setRegulatorBadgeNumber(String regulatorBadgeNumber) {
        this.regulatorBadgeNumber = regulatorBadgeNumber;
    }


//    //bi-directional many-to-one association to Meter
//    @ManyToOne
//    @JoinColumn(name = "GAS_METER_ID")
//    @JsonManagedReference
//    public GasMeter getMeter() {
//        return this.meter;
//    }
//
//    public void setMeter(GasMeter meter) {
//        this.meter = meter;
//    }






}