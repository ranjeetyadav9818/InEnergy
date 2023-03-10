package com.inenergis.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name="ENTITY_HISTORY")
public class History extends IdentifiableEntity {

    @Column(name="ENTITY_NAME")
    private String entity;
    @Column(name="ENTITY_ID")
    private String entityId;
    @Column(name="FIELD_NAME")
    private String field;
    @Column(name="OLD_VALUE",length = 65535,columnDefinition="Text")
    private String oldValue;
    @Column(name="NEW_VALUE",length = 65535,columnDefinition="Text")
    private String newValue;
    @Column(name="CREATION_DATE")
    private Date creationDate;
    @Column(name="AUTHOR")
    private String author;
    @Column(name="CHANGE_TYPE")
    @Enumerated(EnumType.STRING)
    private HistoryChangeType changeType;

    public enum HistoryChangeType{
        FIELD, RELATIONSHIP
    }
}