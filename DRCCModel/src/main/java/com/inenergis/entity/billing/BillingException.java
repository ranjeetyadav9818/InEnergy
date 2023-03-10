package com.inenergis.entity.billing;

import com.inenergis.entity.IdentifiableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "BILLING_EXCEPTION")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingException extends IdentifiableEntity {

    @Column(name = "MESSAGE")
    private String message;

    @ManyToOne
    @JoinColumn(name = "INVOICE_ID")
    private Invoice invoice;
}