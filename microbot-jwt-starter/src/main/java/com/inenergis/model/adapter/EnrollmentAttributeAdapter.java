package com.inenergis.model.adapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by egamas on 16/10/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentAttributeAdapter {
    private String key;
    private String value;
}
