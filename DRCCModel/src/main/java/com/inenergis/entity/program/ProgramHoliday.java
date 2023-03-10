package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter

@ToString(exclude = "profile")
@Entity
@Table(name = "PROGRAM_HOLIDAYS")
public class ProgramHoliday extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Column(name = "NERC")
    private boolean nerc;

    @Column(name = "DATE")
    private Date date;

    public boolean notFilledIn() {
        return (StringUtils.isEmpty(name) && date == null);
    }
}