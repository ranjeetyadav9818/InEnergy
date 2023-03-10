package com.inenergis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "MANUFACTURER")
@Entity
public class Manufacturer extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "POINT_OF_CONTACT")
    private String pointOfContact;

    @Column(name = "PHONE")
    private String phone;
}
