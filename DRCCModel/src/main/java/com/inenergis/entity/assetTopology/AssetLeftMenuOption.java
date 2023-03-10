package com.inenergis.entity.assetTopology;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.CommodityType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by egamas on 09/11/2017.
 */
@Getter
@Setter
@Builder
public class AssetLeftMenuOption extends IdentifiableEntity {

    private NetworkType networkType;

    private CommodityType commodityType;

    private AssetLeftMenuOption parentOperation;

    private List<AssetLeftMenuOption> childMenus;

    private String name;

    private String outcome;

    private Integer order;

    public List<AssetLeftMenuOption> getChildsOrdered() {
        if (CollectionUtils.isEmpty(childMenus)) {
            return new ArrayList<>();
        }
        childMenus.sort(Comparator.comparing(AssetLeftMenuOption::getOrder));
        return new ArrayList<>(childMenus);
    }

}
