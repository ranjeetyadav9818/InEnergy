package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "COMMODITY_SUB_PROGRAM")
public class CommoditySubProgram extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;
}