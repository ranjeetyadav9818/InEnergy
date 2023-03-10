package com.inenergis.model;

import lombok.Data;

import java.util.List;

@Data
public class EligibilityResult {

    private boolean eligible;
    private List<String> errors;
    private List<EligibilityCheck> checks;
}
