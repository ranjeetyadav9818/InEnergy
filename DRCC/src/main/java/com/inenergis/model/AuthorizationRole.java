package com.inenergis.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Antonio on 11/05/2017.
 */
public enum AuthorizationRole {

    VIEW_STATISTICS("viewStatistics", Arrays.asList("View event's detailed info")),
    SUPER_USER("superUser", Arrays.asList("Manage Users","View customers","View service points")),
    RATE_PROFILE_EDIT_MODE("editRoleProfile", Arrays.asList("Edit rate plan profiles")),
    RATE_PROFILE_VIEW_MODE("viewRoleProfile", Arrays.asList("View rate plan profiles"));

    private String roleName;
    private List<String> permissions;

    AuthorizationRole(String name, List<String> permissions) {
        this.roleName = name;
        this.permissions = permissions;
    }

    public String getRoleName() {
        return roleName;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
