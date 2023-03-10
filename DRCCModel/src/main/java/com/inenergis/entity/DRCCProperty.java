package com.inenergis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "DRCC_PROPERTY")
public class DRCCProperty extends IdentifiableEntity {

    @Column(name = "_KEY")
    private String key;

    @Column(name = "_VALUE")
    private String value;
}