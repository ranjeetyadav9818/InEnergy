package com.inenergis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@Table(name = "TEMPERATURE_FORECAST")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TemperatureForecast extends IdentifiableEntity {
    @Column(name = "DEGREES")
    private BigDecimal degrees;

    @Column(name = "DAY")
    private Date date;
}