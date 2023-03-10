package com.inenergis.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by egamas on 22/09/2017.
 */
@Getter
@Setter
@ToString(exclude = {"serviceAgreement", "password"})
@Table(name = "PORTAL_USER")
@Entity
public class PortalUser extends IdentifiableEntity {

    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PHONE")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "SERVICE_AGREEMENT_ID")
    private BaseServiceAgreement serviceAgreement;

    @Transient
    private String passwordNew;

}
