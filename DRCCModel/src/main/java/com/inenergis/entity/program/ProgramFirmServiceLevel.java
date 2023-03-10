package com.inenergis.entity.program;

import com.inenergis.entity.HistoryTracked;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

@Getter
@Setter
@ToString(exclude = {"application"})
@Entity
@Table(name = "PROGRAM_FIRM_SERVICE_LEVEL")
@HistoryTracked(notCheck = {"lastUpdated", "lastUpdatedBy", "id"})
public class ProgramFirmServiceLevel extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID")
    private ProgramServiceAgreementEnrollment application;
    @Column(name = "EFFECTIVE_START_DATE")
    private Date effectiveStartDate;
    @Column(name = "EFFECTIVE_END_DATE")
    private Date effectiveEndDate;
    @Column(name = "FSL_VALUE")
    private BigDecimal value;
    @Column(name = "LAST_UPDATED_DATE")
    private Date lastUpdated;
    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;

    private static final BigDecimal ONE_HUNDRED_BIG_DECIMAL = new BigDecimal("100.0");

    public String getFormattedValue(Locale locale) {
        DecimalFormat nf = (DecimalFormat) NumberFormat.getInstance(locale);
        nf.setParseBigDecimal(true);
        return nf.format(value);
    }

    public ProgramEligibilitySnapshot generateSnapshot(boolean isFslInRange, String overridingReason) {
        ProgramEligibilitySnapshot snapshot = new ProgramEligibilitySnapshot();
        snapshot.setAttribute("FSL");
        snapshot.setValue(getFormattedValue(ConstantsProviderModel.LOCALE));
        snapshot.setEffectiveDate(new Date());
        snapshot.setEligible(isFslInRange);
        snapshot.setOverrideReason(overridingReason);
        snapshot.setApplication(application);
        snapshot.setOverrideUser(lastUpdatedBy);

        return snapshot;
    }
}