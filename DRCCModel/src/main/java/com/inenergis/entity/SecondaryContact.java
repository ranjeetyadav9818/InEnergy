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
@Table(name = "SECONDARY_CONTACT")
public class SecondaryContact extends IdentifiableEntity {

    public SecondaryContact() {
    }

    public SecondaryContact(ServiceAgreement serviceAgreement) {
        this.serviceAgreement = serviceAgreement;
    }

    @ManyToOne
    @JoinColumn(name = "SERVICE_AGREEMENT_ID")
    @JsonManagedReference
    private ServiceAgreement serviceAgreement;

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