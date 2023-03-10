package com.inenergis.entity.contract;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "POC_EMAIL")
@Getter
@Setter
@NoArgsConstructor
public class POCEmail extends IdentifiableEntity {

    @Column(name = "EMAIL")
    private String email;

    @ManyToOne
    @JoinColumn(name = "POC_ID")
    private PointOfContact pointOfContact;

    public POCEmail(PointOfContact pointOfContact) {
        super();
        this.pointOfContact = pointOfContact;
    }
}
