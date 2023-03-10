package com.inenergis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "SUBSTATION")
public class Substation extends IdentifiableEntity {

    @Column(name = "VALUE_DATA")
    private String name;
}