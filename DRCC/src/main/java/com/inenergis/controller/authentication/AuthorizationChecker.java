/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inenergis.controller.authentication;

import com.inenergis.model.AuthorizationRole;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.Agent;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static org.picketlink.idm.model.basic.BasicModel.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a utility bean that may be used by the view layer to determine whether the
 * current user has specific privileges. 
 * 
 * @author Shane Bryzak
 *
 */
@Named
@ViewScoped
public class AuthorizationChecker implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
    private Identity identity;
    
    @Inject 
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;
    
    private Boolean superUser = null;
    private Boolean viewStatistics = null;

    private Map<String, Boolean> userRoles = new HashMap<>();
    private static Map<String, Role> roles = new HashMap<>();


    public boolean authorized(String roleName) {
    	return hasApplicationRole(roleName);
	}

    public boolean authorized(AuthorizationRole role) {
    	return hasApplicationRole(role.getRoleName());
	}

    private boolean hasApplicationRole(String roleName) {
		if (userRoles.containsKey(roleName)) {
			return userRoles.get(roleName);
		}
		Role role = getRole(roleName);
		if(role!=null){
			boolean hasRole = hasRole(this.relationshipManager, this.identity.getAccount(), role);
			userRoles.put(roleName, hasRole);
			return hasRole;
        }
		userRoles.put(roleName, false);
        return false;
    }

	private Role getRole(String roleName) {
    	if(roles.containsKey(roleName)){
    		return roles.get(roleName);
		}
		Role role = BasicModel.getRole(this.identityManager, roleName);
    	roles.put(roleName,role);
		return role;
	}

	public Boolean getSuperUser() {
		if(this.superUser==null){
			this.superUser = this.hasApplicationRole("superUser");
		}
		return superUser;
	}

	public void setSuperUser(Boolean superUser) {
		this.superUser = superUser;
	}

	public Boolean getViewStatistics() {
		if(this.viewStatistics==null){
			this.viewStatistics = this.hasApplicationRole("viewStatistics");
		}
		return viewStatistics;
	}

	public void setViewStatistics(Boolean viewStatistics) {
		this.viewStatistics = viewStatistics;
	}


	public boolean isInenergisEmail() {
		return ((User) this.identity.getAccount()).getEmail().endsWith("@inenergis.com");
	}
}
