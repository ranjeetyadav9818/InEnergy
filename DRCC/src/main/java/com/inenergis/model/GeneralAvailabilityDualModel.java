package com.inenergis.model;

import com.inenergis.entity.program.GeneralAvailability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DualListModel;

@Getter
@Setter
@AllArgsConstructor
public class GeneralAvailabilityDualModel {
    private GeneralAvailability generalAvailability;
    private DualListModel<String> dualListModel;
}
