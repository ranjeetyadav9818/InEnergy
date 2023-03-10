package com.inenergis.entity.assetTopology;

import com.inenergis.entity.asset.Asset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CATALOG_ATTR")
@Getter
@Setter
@NoArgsConstructor
public class CatalogAttribute extends AssetAttribute {

    public CatalogAttribute(AssetProfileAttribute profileAttribute, Asset asset) {
        this.profileAttribute = profileAttribute;
        this.name = profileAttribute.getName();
        this.mandatory = profileAttribute.getMandatory();
        this.order = profileAttribute.getOrder();
        this.attributeValidation = profileAttribute.getAttributeValidation();
        this.asset = asset;
    }
}
