package com.inenergis.util;

import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.genericEnum.EssentialCustomerType;
import com.inenergis.entity.program.ProgramEssentialCustomer;
import com.inenergis.entity.program.ProgramFirmServiceLevel;
import com.inenergis.entity.program.ProgramFirmServiceLevelRule;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FslRange {
    private Long min;
    private Long max;

    private List<Long> avgSummerOnPeakWattList;
    private List<Long> avgWinterPartialPeakWattList;
    private boolean noDataWarning = false;

    Integer dataAvailabilityAllMonths = 0;
    Integer dataAvailabilitySummerMonths = 0;
    Integer dataAvailabilityWinterMonths = 0;

    public FslRange(List<ProgramFirmServiceLevelRule> flsRules, List<Long> avgSummerOnPeakWattList, List<Long> avgWinterPartialPeakWattList) {
        min = 0L;
        max = Long.MAX_VALUE;
        this.avgSummerOnPeakWattList = avgSummerOnPeakWattList;
        this.avgWinterPartialPeakWattList = avgWinterPartialPeakWattList;

        for (ProgramFirmServiceLevelRule rule : flsRules) {
            if (!isRuleActive(rule)) {
                continue;
            }

            Long valueWatts = evaluateRule(rule);

            switch (rule.getComparisonOperator()) {
                case "GE":
                    min = Math.max(min, valueWatts);
                    break;
                case "LE":
                    max = Math.min(max, valueWatts);
                    break;
                case "EQ":
                    min = Math.max(min, valueWatts);
                    max = Math.min(max, valueWatts);
                    break;
                default:
                    break;
            }
        }

        dataAvailabilityAllMonths = dataAvailabilitySummerMonths + dataAvailabilityWinterMonths;

        if (dataAvailabilityAllMonths == 0) {
            noDataWarning = true;
        }
    }

    private boolean isRuleActive(ProgramFirmServiceLevelRule rule) {
        List<EssentialCustomerType> allowedTypes = rule.getProfile().getEssentialCustomers().stream()
                .map(ProgramEssentialCustomer::getEssentialCustomerType)
                .collect(Collectors.toList());

        if (rule.isEssential()) {
            return allowedTypes.contains(EssentialCustomerType.Y);
        } else {
            return allowedTypes.contains(EssentialCustomerType.E) || allowedTypes.contains(EssentialCustomerType.N);
        }
    }

    public boolean isInRange(ProgramFirmServiceLevel fsl) {
        return isInRange(fsl.getValue().longValue());
    }

    private boolean isInRange(long value) {
        return (value > getMinKw() || value == getMinKw()) && (value < getMaxKw() || value == getMaxKw());
    }

    public Long getMinKw() {
        return EnergyUtil.convertWattsTo(min, ElectricalUnit.KW).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
    }

    public Long getMaxKw() {
        return EnergyUtil.convertWattsTo(max, ElectricalUnit.KW).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
    }

    private Long evaluateRule(ProgramFirmServiceLevelRule rule) {
        if ("load_reduction".equals(rule.getType())) {
            return EnergyUtil.convertToWatts(rule.getComparisonValue(), rule.getEnergyUnit());
        } else if ("season".equals(rule.getType())) {
            BigDecimal percent = rule.getComparisonValue();
            BigDecimal value;
            switch (rule.getComparisonReference()) {
                case AVERAGE_SUMMER_ON_PEAK:
                    value = BigDecimal.valueOf(avgSummerOnPeakWattList.stream().mapToLong(a -> a).average().orElse(0));
                    dataAvailabilitySummerMonths = Math.max(dataAvailabilitySummerMonths, avgSummerOnPeakWattList.size());
                    break;
                case MAX_SUMMER_ON_PEAK:
                    value = BigDecimal.valueOf(avgSummerOnPeakWattList.stream().mapToLong(a -> a).max().orElse(0));
                    dataAvailabilitySummerMonths = Math.max(dataAvailabilitySummerMonths, avgSummerOnPeakWattList.size());
                    break;
                case AVERAGE_WINTER_PARTIAL_PEAK:
                    value = BigDecimal.valueOf(avgWinterPartialPeakWattList.stream().mapToLong(a -> a).average().orElse(0));
                    dataAvailabilityWinterMonths = Math.max(dataAvailabilityWinterMonths, avgWinterPartialPeakWattList.size());
                    break;
                case MAX_WINTER_PARTIAL_PEAK:
                    value = BigDecimal.valueOf(avgWinterPartialPeakWattList.stream().mapToLong(a -> a).max().orElse(0));
                    dataAvailabilityWinterMonths = Math.max(dataAvailabilityWinterMonths, avgWinterPartialPeakWattList.size());
                    break;
                default:
                    throw new RuntimeException("Unexpected comparison reference: " + rule.getComparisonReference());
            }

            return value.multiply(percent).divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP).longValue();
        } else {
            throw new RuntimeException("Invalid rule type: " + rule.getType());
        }
    }
}