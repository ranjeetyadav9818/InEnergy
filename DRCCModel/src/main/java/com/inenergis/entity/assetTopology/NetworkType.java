package com.inenergis.entity.assetTopology;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.CommodityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by egamas on 09/11/2017.
 */
@Entity
@Table(name = "NETWORK_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkType extends IdentifiableEntity{

    @Column(name = "COMMODITY_TYPE")
    @Enumerated(EnumType.STRING)
    private CommodityType commodityType;

    @Column(name = "NAME")
    private String name;

    @Column(name = "UI_ORDER")
    private Integer order;

    @OneToMany(mappedBy = "networkType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetProfile> assetProfiles;

    @OneToMany(mappedBy = "networkType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetProfile> assetGroups;

}
