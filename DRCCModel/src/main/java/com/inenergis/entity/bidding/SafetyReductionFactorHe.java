package com.inenergis.entity.bidding;


import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.entity.program.ProgramProfile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Getter
@Setter
@Entity
@Table(name = "SAFETY_REDUCTION_FACTOR")
public class SafetyReductionFactorHe extends IdentifiableEntity {

    @Column(name = "HOUR_END")
    private HourEnd hourEnd;
    @Column(name = "PROGRAM_HOLIDAY")
    private Long programHoliday;
    @Column(name = "MONDAY")
    private Long monday;
    @Column(name = "TUESDAY")
    private Long tuesday;
    @Column(name = "WEDNESDAY")
    private Long wednesday;
    @Column(name = "THURSDAY")
    private Long thursday;
    @Column(name = "FRIDAY")
    private Long friday;
    @Column(name = "SATURDAY")
    private Long saturday;
    @Column(name = "SUNDAY")
    private Long sunday;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Override
    public String toString() {
        return "{" +
                "hourEnd=" + hourEnd +
                ", programHoliday=" + programHoliday +
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                '}';
    }
}
