package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.SubLap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString(exclude = "profile")
@Entity
@Table(name = "SUBLAP_PROGRAM_MAPPING")
public class SublapProgramMapping extends IdentifiableEntity implements Comparable<SublapProgramMapping> {

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @ManyToOne
    @JoinColumn(name = "SUBLAP_CODE", referencedColumnName = "CODE")
    private SubLap subLap;

    @Column(name = "DRMS_PROGRAM_ID")
    private String drmsProgramId;

    @Override
    public int compareTo(SublapProgramMapping mapping) {
        if (mapping.getSubLap() == null) {
            return -1;
        }
        return subLap.getCode().compareTo(mapping.getSubLap().getCode());
    }
}