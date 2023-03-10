package com.inenergis.entity;

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
@Table(name = "EVENT_DISPATCH_LEVEL")
public class EventDispatchLevel extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    Event event;

    @Column(name = "VALUE")
    private String value;

    public EventDispatchLevel() {

    }

    public EventDispatchLevel(String value) {
        this.value = value;
    }

    public EventDispatchLevel(String value, Event event) {
        this.value = value;
        this.event = event;
    }
}