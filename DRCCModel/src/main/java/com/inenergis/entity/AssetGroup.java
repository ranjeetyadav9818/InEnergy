package com.inenergis.entity;

import com.inenergis.entity.genericEnum.CommodityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ASSET_GROUP")
@ToString(of = "name")
@Getter
@Setter
public class AssetGroup extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "LEVEL")
    private Integer level;

    @Column(name = "COMMODITY_TYPE")
    @Enumerated(EnumType.STRING)
    private CommodityType commodityType;

    @ManyToOne
    @JoinColumn(name = "ASSET_GROUP_PARENT_ID")
    private AssetGroup parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetGroup> children;

}
