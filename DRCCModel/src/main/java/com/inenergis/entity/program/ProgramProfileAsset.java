package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.NetworkType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "asset", callSuper = false)
@Table(name = "PROGRAM_PROFILE_ASSET")
public class ProgramProfileAsset extends IdentifiableEntity {

    public ProgramProfileAsset() {
        init();
    }

    private void init() {
        this.assetProfilesByGrid = new ArrayList<>();
        this.assetsByFilter = new ArrayList<>();
    }

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @ManyToOne
    @JoinColumn(name = "ASSET_ID")
    private Asset asset;

    @Transient
    private List<AssetProfile> assetProfilesByGrid;

    @Transient
    private List<Asset> assetsByFilter;

    @Transient
    private NetworkType networkType;

    @Transient
    private AssetProfile assetProfile;

    @PostLoad
    public void postLoad() {
        if (this.asset != null) {
            assetProfile = asset.getAssetProfile();
            if (assetProfile != null) {
                networkType = assetProfile.getNetworkType();
            }
        }
    }
}