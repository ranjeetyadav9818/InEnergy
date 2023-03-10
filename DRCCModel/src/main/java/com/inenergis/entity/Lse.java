package com.inenergis.entity;

import com.inenergis.entity.marketIntegration.Iso;
import lombok.EqualsAndHashCode;
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
@ToString(of = "name")
@EqualsAndHashCode(of = "name", callSuper = false)
@Entity
@Table(name = "LSE")
public class Lse extends IdentifiableEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @ManyToOne
    @JoinColumn(name = "ISO_ID")
    private Iso iso;
}