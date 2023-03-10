package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.EssentialCustomerType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(of = {"essentialCustomer", "profile"}, callSuper = false)
@Entity
@Table(name = "PROGRAM_ESSENTIAL_CUSTOMER")
@ToString(of = "essentialCustomerType")
public class ProgramEssentialCustomer extends IdentifiableEntity {

    @Column(name = "ESSENTIAL_CUSTOMER")
    @Enumerated(EnumType.STRING)
    private EssentialCustomerType essentialCustomerType;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;
}