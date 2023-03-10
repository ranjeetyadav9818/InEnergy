package com.inenergis.entity.genericEnum;

/**
 * Created by egamas on 13/11/2017.
 */
public enum AssetProfileType {

    CATALOG_INVENTORY("Catalog and Inventory"),
    ASSEMBLY("Assembly"),
    CONNECTION("Connection");

    private String name;

    AssetProfileType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
