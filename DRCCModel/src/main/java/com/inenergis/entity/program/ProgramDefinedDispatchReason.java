package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.DispatchReason;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(of = "dispatchReason", callSuper = false)
@Entity
@Table(name = "PROGRAM_DEFINED_DISPATCH_REASON")
public class ProgramDefinedDispatchReason extends IdentifiableEntity {

    @Column(name = "DISPATCH_REASON")
    @Enumerated(EnumType.STRING)
    private DispatchReason dispatchReason;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Override
    public String toString() {
        if (dispatchReason != null) {
            return dispatchReason.getName();
        }

        return null;
    }
}