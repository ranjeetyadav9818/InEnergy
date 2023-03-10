package com.inenergis.model.adapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.billing.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

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
public class PaymentAdapter {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private String type;
    private Long value;
    private String reference;
    private String paidBy;

    public static List<PaymentAdapter> buildPayments(ServiceAgreement sa) {
        final PaymentAdapter.PaymentAdapterBuilder builder = PaymentAdapter.builder();
        List<Payment> payments = sa.getPayments();

        if (CollectionUtils.isNotEmpty(payments)) {
            return payments.stream().map(payment -> build(payment)).collect(Collectors.toList());
        }
        return new ArrayList<>();

    }

    public static PaymentAdapter build(Payment payment){
        return PaymentAdapter.builder()
                .id(payment.getId())
                .date(payment.getDate())
                .type(payment.getType()!=null?payment.getType().getName():null)
                .value(payment.getValue())
                .reference(payment.getReference())
                .paidBy(payment.getPaidBy())
                .build();
    }
}