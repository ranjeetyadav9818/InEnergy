package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.DispatchLevel;
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
@EqualsAndHashCode(of = "dispatchLevel", callSuper = false)
@Entity
@Table(name = "PROGRAM_DEFINED_DISPATCH_LEVEL")
public class ProgramDefinedDispatchLevel extends IdentifiableEntity {

    @Column(name = "DISPATCH_LEVEL")
    @Enumerated(EnumType.STRING)
    private DispatchLevel dispatchLevel;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Override
    public String toString() {
        if (dispatchLevel != null) {
            return dispatchLevel.getName();
        }

        return "";
    }
}