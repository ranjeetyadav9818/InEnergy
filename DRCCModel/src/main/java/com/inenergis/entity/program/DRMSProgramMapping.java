package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.DispatchLevel;
import com.inenergis.entity.genericEnum.DispatchReason;
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
@ToString(exclude = "profile")
@Entity
@Table(name = "DRMS_PROGRAM_MAPPING")
public class DRMSProgramMapping extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Column(name = "DISPATCH_LEVEL")
    @Enumerated(EnumType.STRING)
    private DispatchLevel dispatchLevel;

    @Column(name = "DISPATCH_REASON")
    @Enumerated(EnumType.STRING)
    private DispatchReason dispatchReason;

    @Column(name = "DRMS_PROGRAM_ID")
    private String drmsProgramId;
}