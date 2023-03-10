package com.inenergis.entity.locationRegistration;

import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.Date;

/**
 * This abstract class must be implemented to persist the workflow exceptions produced at Location Registration.
 * The sub clasess are responsible for dealing whith the type of exception attribute.
 * Each subclass should be used inside a processor or module, providing a enum type of allowed values to store in the
 * TYPE column of the table LR_EXCEPTION
 * The field type must be mapped to de field TYPE of the common exceptions table using the annotations as follow
 *  {@literal @}Column(name = "TYPE")
 *  {@literal @}Enumerated(EnumType.TEXT)
 *  private [enum type here] type;
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EXCEPTION_TYPE", discriminatorType = DiscriminatorType.STRING)
@Table(name = "LR_EXCEPTION")
@Getter
@Setter
@EqualsAndHashCode(of = {"type"})
@ToString
public abstract class IsoException extends IdentifiableEntity {

    @Column(name = "TYPE")
    private String type;

    @Column(name = "DATE_ADDED")
    private Date dateAdded;

    @Column(name = "DATE_RESOLVED")
    private Date dateResolved;

    @Column(name = "RESOLVED")
    boolean resolved;

}
