package com.inenergis.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COUNTRY")
@ToString(of = "name")
@Getter
@Setter
@EqualsAndHashCode(of={"code"})
public class Country extends IdentifiableEntity {

    @Column(name="NAME")
    private String name;

    @Column(name="CODE")
    private String code;

}
