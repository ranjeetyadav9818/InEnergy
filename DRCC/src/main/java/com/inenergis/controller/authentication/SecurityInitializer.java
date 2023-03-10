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

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.inenergis.model.AuthorizationRole;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

/**
 * This startup bean creates a number of default users, groups and roles when the application is started.
 * 
 * @author Shane Bryzak
 */
@Singleton
@Startup
public class SecurityInitializer {

    @Inject
    private PartitionManager partitionManager;
    
    Logger log = LoggerFactory.getLogger(SecurityInitializer.class);
   

    @PostConstruct
    public void create() {

    	IdentityManager identityManager = this.partitionManager.createIdentityManager();
    	
    	User defaultUser =  BasicModel.getUser(identityManager, "admin@inenergis.com");
        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();

        if(defaultUser==null){
        // Create user Admin
        User admin = new User("admin@inenergis.com");
        admin.setEmail("admin@inenergis.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        identityManager.add(admin);
        identityManager.updateCredential(admin, new Password("demo"));
        defaultUser = admin;

        // Create user 
        User test1 = new User("kkb9@pge.com");
        test1.setEmail("kkb9@pge.com");
        test1.setFirstName("Karter");
        test1.setLastName("Bui");
        identityManager.add(test1);
        identityManager.updateCredential(test1, new Password("P@ssw0rd"));
        
        // Create user TestUser
        User test2 = new User("a2d1@pge.com");
        test2.setEmail("a2d1@pge.com");
        test2.setFirstName("Amy");
        test2.setLastName("Donis");
        identityManager.add(test2);
        identityManager.updateCredential(test2, new Password("P@ssw0rd"));
        
        // Create user TestUser
        User test3 = new User("9rk@pge.com");
        test3.setEmail("9rk@pge.com");
        test3.setFirstName("Jyoti");
        test3.setLastName("Rao");
        identityManager.add(test3);
        identityManager.updateCredential(test3, new Password("P@ssw0rd"));
        
     // Create user 
        User test4 = new User("k1pf@pge.com");
        test4.setEmail("k1pf@pge.com");
        test4.setFirstName("Ken");
        test4.setLastName("Papagno");
        identityManager.add(test4);
        identityManager.updateCredential(test4, new Password("P@ssw0rd"));
        
        // Create user TestUser
        User test5 = new User("nbl1@pge.com");
        test5.setEmail("nbl1@pge.com");
        test5.setFirstName("Nancy");
        test5.setLastName("Lee");
        identityManager.add(test5);
        identityManager.updateCredential(test5, new Password("P@ssw0rd"));
        
        // Create user TestUser
        User test6 = new User("m2a2@pge.com");
        test6.setEmail("m2a2@pge.com");
        test6.setFirstName("Melody");
        test6.setLastName("Agustin");
        identityManager.add(test6);
        identityManager.updateCredential(test6, new Password("P@ssw0rd"));
        
     // Create user TestUser
        User test7 = new User("r4c1@pge.com");
        test7.setEmail("r4c1@pge.com");
        test7.setFirstName("Randy");
        test7.setLastName("Chiu");
        identityManager.add(test7);
        identityManager.updateCredential(test7, new Password("P@ssw0rd"));
        
        // Create user TestUser
        User test8 = new User("etl4@pge.com");
        test8.setEmail("etl4@pge.com");
        test8.setFirstName("Elwood");
        test8.setLastName("Lao");
        identityManager.add(test8);
        identityManager.updateCredential(test8, new Password("P@ssw0rd"));
        
     // Create user TestUser
        User test9 = new User("pdpsupport@pge.com");
        test9.setEmail("pdpsupport@pge.com");
        test9.setFirstName("PDP");
        test9.setLastName("Support");
        identityManager.add(test9);
        identityManager.updateCredential(test9, new Password("P@ssw0rd"));
        
        // Create user TestUser
        User test10 = new User("srsupport@pge.com");
        test10.setEmail("srsupport@pge.com");
        test10.setFirstName("SR");
        test10.setLastName("Support");
        identityManager.add(test10);
        identityManager.updateCredential(test10, new Password("P@ssw0rd"));
     
        
        // Create admin group
//        Group administrators = new Group("administrators");
//        identityManager.add(administrators);
        
        // Create role "administrator"
//        Role administrator = new Role("administrator");
//        identityManager.add(administrator);
        
        // Create role "viewStatistics"
        Role viewStats = new Role("viewStatistics");
        identityManager.add(viewStats);

        // Create application role "superUser"
        Role superuser = new Role("superUser");
        identityManager.add(superuser);


        // Join group
        BasicModel.grantRole(relationshipManager, admin, superuser);
        BasicModel.grantRole(relationshipManager, admin, viewStats);
        BasicModel.grantRole(relationshipManager, test1, superuser);
        BasicModel.grantRole(relationshipManager, test1, viewStats);
        BasicModel.grantRole(relationshipManager, test2, superuser);
        BasicModel.grantRole(relationshipManager, test2, viewStats);
        BasicModel.grantRole(relationshipManager, test3, superuser);
        BasicModel.grantRole(relationshipManager, test3, viewStats);
        BasicModel.grantRole(relationshipManager, test4, superuser);
        BasicModel.grantRole(relationshipManager, test4, viewStats);
        
        BasicModel.grantRole(relationshipManager, test5, viewStats);
        BasicModel.grantRole(relationshipManager, test6, viewStats);
        BasicModel.grantRole(relationshipManager, test7, viewStats);
        BasicModel.grantRole(relationshipManager, test8, viewStats);
//        BasicModel.grantGroupRole(relationshipManager, admin, administrator, administrators);
//        addToGroup(relationshipManager, admin, administrators);
//        relationshipManager.add(new Grant(admin, superuser));

    	}

        for (AuthorizationRole authorizationRole : AuthorizationRole.values()) {
            String roleName = authorizationRole.getRoleName();
            Role role = BasicModel.getRole(identityManager, roleName);
            if(role == null){
                Role newRole = new Role(roleName);
                identityManager.add(newRole);
                BasicModel.grantRole(relationshipManager, defaultUser, newRole);

            }
        }
    }
}
