package com.inenergis.entity.contract;

import com.inenergis.entity.Country;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.ContractAddressType;
import com.inenergis.entity.genericEnum.USStates;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRACT_ADDRESS")
@Getter
@Setter
@NoArgsConstructor
public class ContractAddress extends IdentifiableEntity{
    @ManyToOne
    @JoinColumn(name = "CONTRACT_ENTITY_ID")
    private ContractEntity contractEntity;

    @Column(name="ADDRESS_TYPE")
    @Enumerated(EnumType.STRING)
    private ContractAddressType addressType;

    @Column(name = "COUNTY")
    private String county;

    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;

    @Column(name = "ADDRESS_1")
    private String address1;

    @Column(name = "ADDRESS_2")
    private String address2;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private USStates state;

    @Column(name = "POST_CODE")
    private String postCode;

    public ContractAddress(ContractEntity contractEntity) {
        super();
        this.contractEntity = contractEntity;
    }
}
