package com.inenergis.controller.asset;

import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.service.AssetGroupService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class AssetGroupsController implements Serializable {

    private TreeNode root;

    @Inject
    private AssetGroupService assetGroupService;

    @Inject
    UIMessage uiMessage;
    private AssetGroup rootGroup;
    private AssetGroup selectedGroup;
    private CommodityType commodityType;

    @PostConstruct
    public void init() {
        commodityType = ParameterEncoderService.getCommodityTypeParameter();
        doInit();
    }

    private void doInit() {
        final List<AssetGroup> allBYComodityType = assetGroupService.getAllBYComodityType(commodityType);
        if (CollectionUtils.isNotEmpty(allBYComodityType)) { //to skip null pointer if no rots are defined
            rootGroup = allBYComodityType.get(0);
            root = generateRoot();
        } else {
            add(null);
        }
    }

    private TreeNode generateRoot() {
        TreeNode defaultRoot = new DefaultTreeNode();
        TreeNode tree = new DefaultTreeNode(rootGroup, defaultRoot);
        tree.setExpanded(true);
        for (AssetGroup assetGroup : rootGroup.getChildren()) {
            assignValuesToParent(tree, assetGroup);
        }
        return defaultRoot;
    }

    private void assignValuesToParent(TreeNode tree, AssetGroup assetGroup) {
        TreeNode children = new DefaultTreeNode(assetGroup, tree);
        children.setExpanded(true);
        for (AssetGroup grandChildren : assetGroup.getChildren()) {
            assignValuesToParent(children, grandChildren);
        }
    }

    public void add(AssetGroup node) {
        AssetGroup group = new AssetGroup();
        group.setParent(node);
        group.setName("New level");
        group.setCommodityType(commodityType);
        if (node != null && node.getLevel() != null) {
            group.setLevel(node.getLevel() + 1);
        } else {
            group.setLevel(0);
        }
        group = assetGroupService.saveOrUpdate(group);
        doInit();
        selectedGroup = assetGroupService.getById(group.getId());
    }

    public void delete(AssetGroup node) {
        assetGroupService.delete(node);
        doInit();
    }

    public void selectGroup(AssetGroup group) {
        selectedGroup = group;
    }

    public void update() {
        assetGroupService.saveOrUpdate(selectedGroup);
        selectedGroup = null;
        doInit();
    }
}