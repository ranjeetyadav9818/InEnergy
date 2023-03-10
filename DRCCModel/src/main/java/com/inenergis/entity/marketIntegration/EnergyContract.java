package com.inenergis.entity.marketIntegration;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inenergis.entity.Feeder;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.Substation;
import com.inenergis.entity.contract.ContractDevice;
import com.inenergis.entity.genericEnum.EnergyContractType;
import com.inenergis.entity.genericEnum.PaymentFrequency;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.program.RatePlan;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(of = {"name"})
@Entity
@Table(name = "ENERGY_CONTRACT")
public class EnergyContract extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private EnergyContractType type;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "AGREEMENT_START_DATE")
    private Date agreementStartDate;

    @Column(name = "AGREEMENT_END_DATE")
    private Date agreementEndDate;

    @Column(name = "PAYMENT_FREQUENCY")
    @Enumerated(EnumType.STRING)
    private PaymentFrequency paymentFrequency;

    @Column(name = "CONTRACT_TERM")
    private Integer contractTerm;

    @Column(name = "BILL_DAY_OF_MONTH")
    private Integer billDayOfMonth;

    @OneToMany(mappedBy = "energyContract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillMonth> billMonths;

    @OneToMany(mappedBy = "energyContract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Party> parties;

    @OneToMany(mappedBy = "energyContract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fee> fees;

    @OneToMany(mappedBy = "energyContract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Credit> credits;

    @OneToMany(mappedBy = "energyContract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commodity> commodities;

    @OneToMany(mappedBy = "energyContract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RelatedContract> relatedContracts;

    @OneToMany(mappedBy = "energyContract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractDevice> contractDevices;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "CONTRACT_SERVICE_AGREEMENT", joinColumns = {@JoinColumn(name = "ENERGY_CONTRACT_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "SERVICE_AGREEMENT_ID", nullable = false)})
    @JsonManagedReference
    private Set<ServiceAgreement> contractServiceAgreements;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "RATE_PLAN_CONTRACT_LINK", joinColumns = {@JoinColumn(name = "ENERGY_CONTRACT_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "RATE_PLAN_ID", nullable = false)})
    private List<RatePlan> ratePlans;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "RESOURCE_CONTRACT_LINK", joinColumns = {@JoinColumn(name = "ENERGY_CONTRACT_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "RESOURCE_ID", nullable = false)})
    private List<IsoResource> resources;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "SUBLAPS_CONTRACT_LINK", joinColumns = {@JoinColumn(name = "ENERGY_CONTRACT_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "SUBLAP_ID", nullable = false)})
    private List<SubLap> subLaps;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "SUBSTATION_CONTRACT_LINK", joinColumns = {@JoinColumn(name = "ENERGY_CONTRACT_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "SUBSTATION_ID", nullable = false)})
    private List<Substation> substations;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "FEEDER_CONTRACT_LINK", joinColumns = {@JoinColumn(name = "ENERGY_CONTRACT_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "FEEDER_ID", nullable = false)})
    private List<Feeder> feeders;

    @PreUpdate
    @PrePersist
    public void onUpdate() {
        lastUpdate = new Date();
    }

    public String getStatus() {
        Date now = new Date();
        boolean active = agreementStartDate != null && agreementStartDate.before(now) && (agreementEndDate == null || agreementEndDate.after(now));
        return active ? "Active" : "Inactive";
    }

    public void clearLinkage() {
        if (ratePlans != null) {
            ratePlans.clear();
        }
        if (resources != null) {
            resources.clear();
        }
        if (subLaps != null) {
            subLaps.clear();
        }
        if (substations != null) {
            substations.clear();
        }
        if (feeders != null) {
            feeders.clear();
        }
    }
}