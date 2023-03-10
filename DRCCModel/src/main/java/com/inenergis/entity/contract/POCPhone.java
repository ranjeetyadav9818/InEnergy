package com.inenergis.entity.contract;


import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.PhoneType;
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

@Entity
@Table(name = "POC_PHONE")
@Getter
@Setter
@NoArgsConstructor
public class POCPhone extends IdentifiableEntity{

    @Column(name="NUMBER")
    private String number;

    @Column(name="TYPE")
    @Enumerated(EnumType.STRING)
    private PhoneType type;

    @ManyToOne
    @JoinColumn(name="POC_ID")
    private PointOfContact pointOfContact;

    public POCPhone(PointOfContact pointOfContact) {
        super();
        this.pointOfContact = pointOfContact;
    }
}
