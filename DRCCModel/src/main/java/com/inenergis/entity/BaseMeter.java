package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="BASE_METER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "COMMODITY", discriminatorType = DiscriminatorType.STRING)
public class BaseMeter implements Serializable, VModelEntity {

    private static final long serialVersionUID = 1L;
    private String meterId;
    private String badgeNumber;
    private String smStatus;
    private Date installDate;
    private Date uninstallDate;
    private String smModuleMfr;
    private String configType;
    private String readFreq;
    private String mfg;
    private List<BaseServicePoint> servicePoints;

    public BaseMeter() {
    }


    @Id
    @Column(name="METER_ID")
    public String getMeterId() {
        return this.meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    @Column(name="MTR_BADGENBR")
    public String getBadgeNumber() {
        return badgeNumber;
    }


    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    @Column(name="SM_STATUS")
    public String getSmStatus() {
        return smStatus;
    }


    public void setSmStatus(String smStatus) {
        this.smStatus = smStatus;
    }

    @Column(name="MTR_INSTALL_DT")
    public Date getInstallDate() {
        return installDate;
    }


    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    @Column(name="MTR_UNINSTALL_DT")
    public Date getUninstallDate() {
        return uninstallDate;
    }


    public void setUninstallDate(Date uninstallDate) {
        this.uninstallDate = uninstallDate;
    }

    @Column(name="SM_MODULE_MFR")
    public String getSmModuleMfr() {
        return smModuleMfr;
    }


    public void setSmModuleMfr(String smModuleMfr) {
        this.smModuleMfr = smModuleMfr;
    }

    @Column(name="MTR_CONFIG_TYPE")
    public String getConfigType() {
        return configType;
    }


    public void setConfigType(String configType) {
        this.configType = configType;
    }

    @Column(name="MTR_READ_FREQ")
    public String getReadFreq() {
        return readFreq;
    }


    public void setReadFreq(String readFreq) {
        this.readFreq = readFreq;
    }

    @Column(name="MTR_MFG")
    public String getMfg() {
        return mfg;
    }


    public void setMfg(String mfg) {
        this.mfg = mfg;
    }


    //bi-directional many-to-one association to BaseServicePoint
    @OneToMany(mappedBy="meter")
    @JsonBackReference
    public List<BaseServicePoint> getServicePoints() {
        return this.servicePoints;
    }

    public void setServicePoints(List<BaseServicePoint> servicePoints) {
        this.servicePoints = servicePoints;
    }

    public BaseServicePoint addServicePoint(BaseServicePoint servicePoint) {
        getServicePoints().add(servicePoint);
        servicePoint.setMeter(this);

        return servicePoint;
    }

    public BaseServicePoint removeServicePoint(BaseServicePoint servicePoint) {
        getServicePoints().remove(servicePoint);
        servicePoint.setMeter(null);

        return servicePoint;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseMeter meter = (Meter) o;

        if (meterId != null ? !meterId.equals(meter.meterId) : meter.meterId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return meterId != null ? meterId.hashCode() : 0;
    }

    public String idFieldName() {
        return "meterId";
    }

    public List<String> relationshipFieldNames() {
        return null;
    }

    public List<String> excludedFieldsToCompare() {
        return Arrays.asList("servicePoints");
    }
}
