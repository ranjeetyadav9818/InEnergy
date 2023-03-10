package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.bidding.RiskCondition;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.ComodityType;
import com.inenergis.entity.genericEnum.IsoApplicableContractEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(of = {"name", "effectiveStartDate"})
@Entity
@Table(name = "MI_ISO_PROFILE")
public class IsoProfile extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "ISO_ID")
    private Iso iso;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "EFFECTIVE_START_DATE")
    private Date effectiveStartDate;

    @Column(name = "EFFECTIVE_END_DATE")
    private Date effectiveEndDate;
    //changes
    @Column(name = "APPLICABLE_CONTRACT")
    @Enumerated(EnumType.STRING)
    private IsoApplicableContractEnum isoApplicableContractEnum;


    @Column(name = "MARKET_ELIGIBLE")
    private boolean marketEligible;


    @Column(name = "MARKET_ELIGIBLE_DATE")
    private Date marketEligibleDate;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IsoHoliday> holidays;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IsoProduct> products;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RiskCondition> riskConditions;

    @Column(name = "UDC_ID")
    private String udcId;

    @Column(name = "DRP_ID")
    private String drpId;

    @Column(name = "SC_ID")
    private String scId;

    @Column(name = "COMMODITY")
    @Enumerated(EnumType.STRING)
    private ComodityType commodityType;

    @PreUpdate
    @PrePersist
    public void onUpdate() {
        lastUpdate = new Date();
    }
}