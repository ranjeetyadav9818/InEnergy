package com.inenergis.controller.general;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.controller.authentication.AuthorizationChecker;
import com.inenergis.entity.assetTopology.AssetLeftMenuOption;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.program.Program;
import com.inenergis.service.IsoService;
import com.inenergis.service.NetworkTypeService;
import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
public class LeftMenu implements Serializable {

    @Inject
    private ProgramServiceContract programService;

    @Inject
    private IsoService isoService;

    @Inject
    private AuthorizationChecker authorizationChecker;

    @Inject
    private NetworkTypeService networkTypeService;

    private List<Program> programs;
    private List<Iso> isos;
    private List<AssetLeftMenuOption> assetSubMenus;

    @PostConstruct
    public void init() {
        programs = programService.getPrograms();
        isos = isoService.getIsos();
        assetSubMenus = getMenusOrderByLevel();
    }

    public List<AssetLeftMenuOption> getMenusOrderByLevel() {

        final List<NetworkType> networkTypes = networkTypeService.getAll().stream()
                .sorted(Comparator.comparing(NetworkType::getOrder))
                .collect(Collectors.toList());

        final List<CommodityType> commdities = networkTypes.stream()
                .map(NetworkType::getCommodityType)
                .distinct()
                .sorted(Comparator.comparing(CommodityType::getName, Collator.getInstance()))
                .collect(Collectors.toList());

        List<AssetLeftMenuOption> rootMenus = new ArrayList<>();
        for (CommodityType commdity : commdities) {

            int order = 0; //Order for each commodity

            final AssetLeftMenuOption commodityRootMenu = AssetLeftMenuOption.builder()
                    .commodityType(commdity)
                    .name(commdity.getName())
                    .order(order++)
                    .childMenus(new ArrayList<>())
                    .build();
            rootMenus.add(commodityRootMenu);

            commodityRootMenu.getChildMenus().add(
                    AssetLeftMenuOption.builder()
                            .commodityType(commdity)
                            .name("Asset Profiles")
                            .outcome("AssetTypeProfile")
                            .order(order++)
                            .parentOperation(commodityRootMenu).build());

            commodityRootMenu.getChildMenus().add(
                    AssetLeftMenuOption.builder()
                            .commodityType(commdity)
                            .name("Asset Groups")
                            .outcome("AssetGroups")
                            .order(order++)
                            .parentOperation(commodityRootMenu).build());


            final AssetLeftMenuOption catalogInventory = AssetLeftMenuOption.builder()
                    .commodityType(commdity)
                    .name("Catalogs and Inventories")
                    .order(order++)
                    .parentOperation(commodityRootMenu)
                    .childMenus(new ArrayList<>())
                    .build();

            commodityRootMenu.getChildMenus().add(catalogInventory);

            for (NetworkType networkType : networkTypes) {

                if (networkType.getCommodityType() == commdity) {

                    catalogInventory.getChildMenus().add(
                            AssetLeftMenuOption.builder()
                                    .commodityType(commdity)
                                    .networkType(networkType)
                                    .outcome("AssetCatalog")
                                    .name(networkType.getName() + " Catalog")
                                    .order(order++)
                                    .parentOperation(catalogInventory).build());

                    catalogInventory.getChildMenus().add(
                            AssetLeftMenuOption.builder()
                                    .commodityType(commdity)
                                    .networkType(networkType)
                                    .outcome("AssetDeviceInventory")
                                    .name(networkType.getName() + " Inventory")
                                    .order(order++)
                                    .parentOperation(catalogInventory).build());
                }
            }
            commodityRootMenu.getChildMenus().add(
                    AssetLeftMenuOption.builder()
                            .commodityType(commdity)
                            .name("Map")
                            .outcome("PipelineMap")
                            .order(order++)
                            .parentOperation(commodityRootMenu).build());

            commodityRootMenu.getChildMenus().add(
                                AssetLeftMenuOption.builder()
                                        .commodityType(commdity)
                                        .name("Import/Export")
                                        .outcome("CatalogAndInventoriesImportExport")
                                        .order(order)
                                        .parentOperation(commodityRootMenu).build());
        }
        return rootMenus;
    }


}