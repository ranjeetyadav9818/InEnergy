package com.inenergis.entity.billing;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "PAYMENT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends IdentifiableEntity {

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(name = "DATE")
    private LocalDateTime date;

    @Column(name = "VALUE")
    private Long value;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "PAID_BY")
    private String paidBy;

    @ManyToOne
    @JoinColumn(name = "SERVICE_AGREEMENT_ID")
    @JsonManagedReference
    private BaseServiceAgreement serviceAgreement;
}