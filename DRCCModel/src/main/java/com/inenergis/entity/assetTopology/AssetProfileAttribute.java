package com.inenergis.entity.assetTopology;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.AttributeValidation;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@Table(name = "ASSET_PROFILE_ATTRIBUTE")
@Entity
public abstract class AssetProfileAttribute extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "MANDATORY")
    private Boolean mandatory;

    @Column(name = "ATTR_ORDER")
    private Long order;

    @Column(name="VALIDATION")
    @Enumerated(EnumType.STRING)
    private AttributeValidation attributeValidation;

    @ManyToOne
    @JoinColumn(name = "ASSET_PROFILE_ID")
    private AssetProfile assetProfile;
}
