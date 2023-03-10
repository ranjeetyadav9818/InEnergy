package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the AGREEMENT_POINT_MAP database table.
 */
@Entity
@Table(name = "AGREEMENT_POINT_MAP")
@NamedQuery(name = "AgreementPointMap.findAll", query = "SELECT a FROM AgreementPointMap a")
public class AgreementPointMap implements Serializable, VModelEntity {
    private static final long serialVersionUID = 1L;
    private AgreementPointMapPK id;
    private Date endDate;
    private Date startDate;
    private BaseServiceAgreement serviceAgreement;
    private BaseServicePoint servicePoint;
    private String saSpId;
    private String commodity;

    public AgreementPointMap() {
    }


    @EmbeddedId
    public AgreementPointMapPK getId() {
        return this.id;
    }

    public void setId(AgreementPointMapPK id) {
        this.id = id;
    }

    @Transient
    public String getRowKey() {
        return this.getId().getServiceAgreementId() + this.getId().getServicePointId();
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    //bi-directional many-to-one association to ServiceAgreement
    @ManyToOne
    @JoinColumn(name = "SERVICE_AGREEMENT_ID", insertable = false, updatable = false)
    @JsonManagedReference
    public BaseServiceAgreement getServiceAgreement() {
        return this.serviceAgreement;
    }

    public void setServiceAgreement(BaseServiceAgreement serviceAgreement) {
        this.serviceAgreement = serviceAgreement;
    }


    //bi-directional many-to-one association to ServicePoint
    @ManyToOne
    @JoinColumn(name = "SERVICE_POINT_ID", insertable = false, updatable = false)
    public BaseServicePoint getServicePoint() {
        return this.servicePoint;
    }

    public void setServicePoint(BaseServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }

    @Column(name = "SA_SP_ID")
    public String getSaSpId() {
        return saSpId;
    }


    public void setSaSpId(String saSpId) {
        this.saSpId = saSpId;
    }



    @Column(name = "COMMODITY")
    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgreementPointMap that = (AgreementPointMap) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String idFieldName() {
        return "id";
    }

    public List<String> relationshipFieldNames() {
        return Arrays.asList("serviceAgreement", "servicePoint");
    }

    public List<String> excludedFieldsToCompare() {
        return Arrays.asList("startDate", "endDate");
    }
}