package com.inenergis.entity.award;

import com.inenergis.entity.Event;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.locationRegistration.IsoResource;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "AWARD_EXCEPTION")
public class AwardException extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "AWARD_ID")
    private Award award;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    Event event;

    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "STATUS")
    String status;
    @Column(name = "DATE_ADDED")
    private Date dateAdded;

    @Column(name = "DATE_RESOLVED")
    private Date dateResolved;

}
