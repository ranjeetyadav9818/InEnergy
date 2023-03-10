package com.inenergis.entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

    @Getter
    @Setter
    @ToString(of = "name")
    @Entity
    @Table(name = "CONTRACT_TYPE")

    public class ContractType extends IdentifiableEntity {

        @Column(name = "APPLICABLE_CONTRACT_TYPE")
        private String name;


    }
