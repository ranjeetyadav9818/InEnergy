package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter

@ToString(exclude = "profile")
@Entity
@Table(name = "MI_ISO_PROFILE_HOLIDAY")
public class IsoHoliday extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "MI_ISO_PROFILE_ID")
    private IsoProfile profile;

    @Column(name = "NERC")
    private boolean nerc;

    @Column(name = "DATE")
    private Date date;

}