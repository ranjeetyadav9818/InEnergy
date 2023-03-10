package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
//@Table(name="GAS_METER")
@NamedQuery(name="GasMeter.findAll", query="SELECT m FROM GasMeter m")
@DiscriminatorValue("Gas")
public class GasMeter extends BaseMeter  {


}