package com.inenergis.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name="DOCUMENT")
public class Document extends IdentifiableEntity {

    @Column(name="ENTITY_NAME")
    private String entity;
    @Column(name="ENTITY_ID")
    private String entityId;
    @Column(name="URL")
    private String url;
    @Column(name="FILE_NAME")
    private String fileName;
    @Column(name="CREATION_DATE")
    private Date creationDate;
    @Column(name="AUTHOR")
    private String author;
    @Column(name="CONTENT_TYPE")
    private String contentType;
}