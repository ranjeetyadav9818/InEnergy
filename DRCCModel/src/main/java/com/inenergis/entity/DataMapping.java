package com.inenergis.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Getter
@Setter
@ToString(of = "source")
@EqualsAndHashCode(of = "source", callSuper = false)
@Entity
@Table(name = "DATA_MAPPING")
public class DataMapping extends IdentifiableEntity {
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private DataMappingType type;

    @Column(name = "SRC")
    private String source;

    @Column(name = "DEST")
    private String destination;

    @Column(name = "DEST_CODE")
    private String destinationCode;
}