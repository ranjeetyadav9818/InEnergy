package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inenergis.entity.asset.Asset;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ASSET_SERVICE_AGREEMENT")
@EqualsAndHashCode(of = {"serviceAgreement", "asset"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAgreementAsset extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "SERVICE_AGREEMENT_ID")
    @JsonManagedReference
    private BaseServiceAgreement serviceAgreement;

    @ManyToOne
    @JoinColumn(name = "ASSET_ID")
    private Asset asset;
}
