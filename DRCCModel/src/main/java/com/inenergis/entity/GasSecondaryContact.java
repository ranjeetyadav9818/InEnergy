package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "GAS_SECONDARY_CONTACT")
public class GasSecondaryContact extends IdentifiableEntity {

    public GasSecondaryContact() {
    }

    public GasSecondaryContact(GasServiceAgreement serviceAgreement) {
        this.serviceAgreement = serviceAgreement;
    }

    @ManyToOne
    @JoinColumn(name = "GAS_SERVICE_AGREEMENT_ID")
    @JsonManagedReference
    private GasServiceAgreement serviceAgreement;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "EMAIL")
    private String email;
}