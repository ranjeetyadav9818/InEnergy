package com.inenergis.entity.assetTopology;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.genericEnum.AttributeValidation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@Table(name = "ASSET_ATTRIBUTE")
@NoArgsConstructor
public abstract class AssetAttribute extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "PROFILE_ATTRIBUTE_ID")
    protected AssetProfileAttribute profileAttribute;

    @Column(name = "STRING_VALUE")
    private String stringValue;

    @Column(name = "NUMBER_VALUE")
    private BigDecimal numberValue;

    @Column(name = "DATE_VALUE")
    private LocalDateTime dateValue;

    //Profile attribute fields:

    @Column(name = "NAME")
    protected String name;

    @Column(name = "MANDATORY")
    protected Boolean mandatory;

    @Column(name = "ATTR_ORDER")
    protected Long order;

    @Column(name = "VALIDATION")
    @Enumerated(EnumType.STRING)
    protected AttributeValidation attributeValidation;

    @ManyToOne
    @JoinColumn(name = "DEVICE_ID")
    private AssetDevice device;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ASSET_ID")
    protected Asset asset;

    @Transient
    public void setAttribute(String rawValue) throws IndexOutOfBoundsException {
        switch (profileAttribute.getAttributeValidation()) {
            case TEXT:
                stringValue = rawValue;
                break;
            case NUMBER:
                numberValue = BigDecimal.valueOf(Double.parseDouble(rawValue));
                break;
            case DATE:
                dateValue = LocalDate.parse(rawValue, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                break;
        }
    }
    @Transient
    public String printValue() {
        if (getStringValue() != null) {
            return getStringValue();
        }
        if (getNumberValue() != null) {
            final DecimalFormat f = new DecimalFormat("##.###");
            return f.format(getNumberValue().doubleValue());
        }
        if (getDateValue() != null){
            return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(getDateValue());
        }
        return null;
    }

}