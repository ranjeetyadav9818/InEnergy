package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the METER database table.
 * 
 */
@Entity
//@Table(name="METER")
@NamedQuery(name="Meter.findAll", query="SELECT m FROM Meter m")
@DiscriminatorValue("Electricity")
public class Meter extends BaseMeter {

}