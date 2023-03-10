package com.inenergis.model.adapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.billing.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 25/09/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionAdapter {
    private String servicePointId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private LocalDate month;
    private Long consumption;
}