package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.ComodityType;
import com.inenergis.entity.genericEnum.ProgramType;
import com.inenergis.entity.workflow.WorkPlan;
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
@Table(name = "PROGRAM")
//@CacheAll
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Program extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "CAP_ACTIVE")
    private boolean capActive;

    @Column(name = "CAP_NUMBER")
    private BigDecimal capNumber;

    @Column(name = "CAP_UNIT")
    @Enumerated(EnumType.STRING)
    private CapUnit capUnit;

    @Column(name = "GAS_CAP_UNIT")
    @Enumerated(EnumType.STRING)
    private GasCapUnit gasCapUnit;

    @Column(name = "COMMODITY")
    @Enumerated(EnumType.STRING)
    private ComodityType commodity;

    @Column(name = "PROGRAM_TYPE")
    @Enumerated(EnumType.STRING)
    private ProgramType programType;

    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private List<ProgramProfile> profiles;
    
    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private List<WorkPlan> workPlans;

    @Transient
    public ProgramProfile getActiveProfile() {
        List<ProgramProfile> profiles = getProfiles();
        if (profiles != null) {
            for (ProgramProfile profile : getProfiles()) {
                Date now = new Date();
                if ((now.after(profile.getEffectiveStartDate()) || now.equals(profile.getEffectiveStartDate())) &&
                        (profile.getEffectiveEndDate() == null || now.before(profile.getEffectiveEndDate()))) {
                    return profile;
                }
            }
        }
        return null;
    }

    public boolean isCapNumberValid() {
        if (capActive && capNumber != null && capNumber.scale() > 0 && capUnit == CapUnit.Customer) {
            return false;
        }

        return true;
    }
}
