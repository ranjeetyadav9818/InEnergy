package com.inenergis.entity.contract;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.CompanySector;
import com.inenergis.entity.marketIntegration.Party;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "CONTRACT_ENTITY")
@Getter
@Setter
public class ContractEntity extends IdentifiableEntity {
    @Column(name = "BUSINESS_NAME")
    String businessName;

    @Column(name = "DBA")
    String dba;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    ContractEntity parentCompany;

    @OneToMany(mappedBy = "parentCompany", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ContractEntity> contractEntities;

    @OneToMany(mappedBy = "contractEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<ContractAddress> contractAddresses;

    @OneToMany(mappedBy = "contractEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<PointOfContact> pointOfContacts;

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Device> devices;

    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Party> parties;

    @Column(name = "SECTOR")
    @Enumerated(EnumType.STRING)
    CompanySector sector;

    @Column(name = "TAX_ID")
    String taxId;

    public ContractEntity() {
        super();
        this.contractEntities = new ArrayList<>();
        this.contractAddresses = new ArrayList<>();
        this.pointOfContacts = new ArrayList<>();
        this.devices = new ArrayList<>();
        this.parties = new ArrayList<>();
    }

    public String getAllAddresses() {
        if (CollectionUtils.isEmpty(contractAddresses)) {
            return null;
        }
        return String.join(" | ", contractAddresses.stream().map(a -> a.getAddress1()).collect(Collectors.toList()));
    }

    public String getAllCities() {
        if (CollectionUtils.isEmpty(contractAddresses)) {
            return null;
        }
        return String.join(" | ", contractAddresses.stream().map(a -> a.getCity()).collect(Collectors.toList()));
    }

    public String getAllStates() {
        if (CollectionUtils.isEmpty(contractAddresses)) {
            return null;
        }
        return String.join(" | ", contractAddresses.stream().map(a -> a.getState().getLabel()).collect(Collectors.toList()));
    }

    public String getAllZips() {
        return contractAddresses.stream()
                .map(ContractAddress::getPostCode)
                .collect(Collectors.joining("|"));
    }
}
