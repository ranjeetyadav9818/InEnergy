package com.inenergis.entity.assetTopology;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by egamas on 14/11/2017.
 */
@Entity
@Getter
@Setter
@DiscriminatorValue("CONNECTION")
@NoArgsConstructor
public class ConnectionProfileAttribute extends AssetProfileAttribute {

}
