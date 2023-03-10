package com.inenergis.entity;

import com.inenergis.entity.genericEnum.RiskType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CapacityRisk implements HourEndObject {

    public String uiName = "Capacity Risk";

    public String type = "risk";

    private List<Boolean> capacityValid = new ArrayList<>();

    @Override
    public Object getHourEnd(int hour) {
        return capacityValid.get(0) ? "Low" : "High";
    }

    public CapacityRisk() {
        for (int i = 0; i < 24; i++) {
            capacityValid.add(true);
        }
    }

    public RiskType getRisk() {
        for (int i = 0; i < 24; i++) {
            if (!capacityValid.get(i)) {
                return RiskType.High;
            }
        }

        return RiskType.Low;
    }
}