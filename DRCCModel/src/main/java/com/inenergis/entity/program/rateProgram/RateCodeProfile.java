package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.program.RatePlanProfile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Comparator;

@Getter
@Setter
@Entity
@Table(name = "RATE_CODE_PROFILE")
public class RateCodeProfile extends IdentifiableEntity{

    @ManyToOne
    @JoinColumn(name = "RATE_CODE_ID")
    private RateCode rateCode;

    @Column(name = "ORDER_NUMBER")
    private Long order;

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    public static Comparator<RateCodeProfile> getComparator(){
        return new Comparator<RateCodeProfile>() {
            @Override
            public int compare(RateCodeProfile r1, RateCodeProfile r2) {
                Long order1 = -1L;
                Long order2 = -1L;
                if (r1 != null && r1.getOrder() != null) {order1 = r1.getOrder();}
                if (r2 != null && r2.getOrder() != null) {order2 = r2.getOrder();}
                return order1.compareTo(order2);
            }
        };
    }

}
