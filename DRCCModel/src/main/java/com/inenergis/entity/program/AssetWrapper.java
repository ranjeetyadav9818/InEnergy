package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreementAsset;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AssetWrapper extends IdentifiableEntity {
    private String servicePoint;
    private String program;
    private String deviceName;
    private String deviceType;
    private String usage;
    private String manufacturer;
    private String model;
    private String serial;
    private Date installed;
    private String owner;
    private ServiceAgreementAsset serviceAgreementAsset;
}