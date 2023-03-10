package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
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
@EqualsAndHashCode(of = "asset", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RATE_PLAN_PROFILE_ASSET")
public class RatePlanProfileAsset extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @ManyToOne
    @JoinColumn(name = "ASSET_ID")
    private Asset asset;
}