package com.inenergis.billingEngine.sa;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICE_POINT")
@Data
public class ServicePoint {
    @Id
    @Column(name = "SERVICE_POINT_ID")
    private String servicePointId;
}