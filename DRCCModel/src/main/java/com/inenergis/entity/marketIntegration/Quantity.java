package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.CommodityFrequency;
import com.inenergis.entity.genericEnum.CommodityUnit;
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
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString(exclude = "commodity")
@Entity
@Table(name = "ENERGY_CONTRACT_QUANTITY")
public class Quantity extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "ENERGY_CONTRACT_COMMODITY_ID")
    private Commodity commodity;

    @Column(name = "MINIMUM_QUALITY")
    private BigDecimal minimumQuality;

    @Column(name = "MINIMUM_QUALITY_UNIT")
    @Enumerated(EnumType.STRING)
    private CommodityUnit minimumQualityUnit;

    @Column(name = "MAXIMUM_QUALITY")
    private BigDecimal maximumQuality;

    @Column(name = "MAXIMUM_QUALITY_UNIT")
    @Enumerated(EnumType.STRING)
    private CommodityUnit maximumQualityUnit;

    @Column(name = "DATE_FROM")
    private Date dateFrom;

    @Column(name = "DATE_TO")
    private Date dateTo;

    @Column(name = "COMMODITY_FREQUENCY")
    @Enumerated(EnumType.STRING)
    private CommodityFrequency frequency;


    public Quantity() {
    }

    public Quantity(Commodity commodity) {
        this.commodity = commodity;
    }
}