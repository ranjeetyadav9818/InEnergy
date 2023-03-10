package com.inenergis.entity.award;


import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "AWARD_INSTRUCTION_DETAIL")
public class InstructionDetail extends IdentifiableEntity{

    @ManyToOne
    @JoinColumn(name = "INSTRUCTION_ID")
    Instruction instruction;
    @Column(name = "SERVICE_TYPE")
    private String serviceType;
    @Column(name = "CAPACITY")
    private Long capacity;
    @Column(name = "ISO_SEQ")
    private Integer segNo;

}
