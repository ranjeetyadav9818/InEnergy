package com.inenergis.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "NOTES")
public class Note extends IdentifiableEntity {

    @Column(name = "ENTITY")
    private String entity;
    @Column(name = "ENTITY_ID")
    private String entityId;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "AUTHOR")
    private String author;
    @Column(name = "CREATION_DATE")
    private Date creationDate;
}