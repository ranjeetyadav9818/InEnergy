package com.inenergis.entity.program.rateProgram;


import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.RateCodeSector;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString(of = "name")
@Entity
@Table(name = "SECTORS_FOR_RATE_CODE")
public class RateCodeSectors extends IdentifiableEntity{

    @Column(name = "SECTOR")
    @Enumerated(EnumType.STRING)
    private RateCodeSector sector;

    @ManyToOne
    @JoinColumn(name="RATE_CODE_ID")
    private RateCode rateCode;
}
