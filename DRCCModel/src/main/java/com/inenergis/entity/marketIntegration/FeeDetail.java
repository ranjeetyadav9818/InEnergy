package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.FeeDetailUnit;
import com.inenergis.entity.genericEnum.FeeIndex;
import com.inenergis.entity.genericEnum.FeeUnitOfMeasure;
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
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ENERGY_CONTRACT_FEE_DETAIL")
@NoArgsConstructor
public class FeeDetail extends IdentifiableEntity {


    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "FEE_INDEX")
    @Enumerated(EnumType.STRING)
    private FeeIndex index;

    @Column(name = "UNIT_OF_MEASURE") //Mponth day etc
    @Enumerated(EnumType.STRING)
    private FeeUnitOfMeasure unitOfMeasure;

    @Column(name = "CAP_FROM")
    private Long from;

    @Column(name = "CAP_TO")
    private Long to;

    @Column(name = "DATE_FROM")
    private Date dateFrom;

    @Column(name = "DATE_TO")
    private Date dateTo;

    @Column(name = "FEE_UNIT")
    @Enumerated(EnumType.STRING)
    private FeeDetailUnit feeUnit;

    @ManyToOne
    @JoinColumn(name = "FEE_ID")
    private Fee fee;

    public FeeDetail(Fee fee) {
        this.fee = fee;
    }
}
