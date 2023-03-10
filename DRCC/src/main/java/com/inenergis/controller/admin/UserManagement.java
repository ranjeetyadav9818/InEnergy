package com.inenergis.controller.admin;

import com.inenergis.model.AuthorizationRole;
import com.inenergis.util.UIMessage;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.picketlink.authorization.annotations.Restrict;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class UserManagement implements Serializable{

	private static final long serialVersionUID = 1L;

	Logger log =  LoggerFactory.getLogger(UserManagement.class);

	@Inject
	EntityManager entityManager;
	private IdentityManager identityManager;

	@Inject
	UIMessage uiMessage;


	@Inject
	private PartitionManager partitionManager;

	@Email
	private String email;
	private User user;
	private String password;
	private User selectedUser;

	List<User> users;
	DualListModel<AuthorizationRole> roles = new DualListModel<>();
	private List<AuthorizationRole> rolesAvailable;
	private RelationshipManager relationshipManager;

	@PostConstruct
	public void onCreate(){
		identityManager = this.partitionManager.createIdentityManager();
		relationshipManager = this.partitionManager.createRelationshipManager();
		rolesAvailable =new ArrayList<>();
		for (AuthorizationRole authorizationRole : AuthorizationRole.values()) {
			rolesAvailable.add(authorizationRole);
		}
	}

	@PreDestroy
	public void onDestroy(){

	}
	
	public List<User> getUsers(){
		if(users==null){
			IdentityManager identityManager = partitionManager.createIdentityManager();
			IdentityQueryBuilder builder = identityManager.getQueryBuilder();
			IdentityQuery query = builder.createIdentityQuery(User.class);
			users = query.getResultList();
		}
		return users;
	}
	
	@Restrict("#{hasRole('superUser')}")
	public void add(){
		user = new User();
	}
	
	@Restrict("#{hasRole('superUser')}")
	public void saveUser(){
		user.setLoginName(getEmail());
		user.setEmail(user.getLoginName());
		IdentityManager identityManager = this.partitionManager.createIdentityManager();
		identityManager.add(user);
		final Password p = new Password(this.getPassword());
		identityManager.updateCredential(user, p, new Date(), new Date());
		users = null;
	    uiMessage.addMessage("Added User {0} {1}", user.getFirstName(),user.getLastName());
		user = null;
		
	}
	
	@Restrict("#{hasRole('superUser')}")
	public void cancelSaveUser(){
		this.password = null;
		this.user = null;
	}
	
	@Restrict("#{hasRole('superUser')}")
	public void onRowEdit(RowEditEvent event) {		
		User u = (User) event.getObject();
		User a = (User) identityManager.lookupById(Account.class, u.getId());
		a.setEmail(u.getEmail());
		a.setLoginName(u.getEmail());
		a.setFirstName(u.getFirstName());
		a.setLastName(u.getLastName());
		a.setEnabled(u.isEnabled());
		identityManager.update(a);
		if(StringUtils.isNotBlank(this.password)){
			identityManager.updateCredential(a, new Password(this.password));
		}
    }
	
	@Restrict("#{hasRole('superUser')}")
	public void deleteUser(){
		if(this.selectedUser!=null){
			this.users.remove(this.selectedUser);
			IdentityManager identityManager = this.partitionManager.createIdentityManager();
			User a = (User) identityManager.lookupById(Account.class, selectedUser.getId());
			log.info("deleting User {}", a);
			identityManager.remove(a);
			this.selectedUser = null;
		}
	}
     
	@Restrict("#{hasRole('superUser')}")
    public void onRowCancel(RowEditEvent event) {
    	this.user = (User)event.getObject();
    }

	public void onUserSelection(){
		roles.setSource(new ArrayList<>());
		roles.setTarget(new ArrayList<>());
		for (AuthorizationRole authRole: rolesAvailable) {
			final Role role = BasicModel.getRole(identityManager, authRole.getRoleName());
			if(BasicModel.hasRole(relationshipManager,selectedUser,role)){
				roles.getTarget().add(authRole);
			}else{
				roles.getSource().add(authRole);
			}
		}
	}

	public void assignRoles(){
		for (AuthorizationRole sourceRoleName : roles.getSource()) {
			final Role sourceRole = BasicModel.getRole(identityManager, sourceRoleName.getRoleName());
			if (BasicModel.hasRole(relationshipManager, selectedUser, sourceRole)) {
				BasicModel.revokeRole(relationshipManager, selectedUser, sourceRole);
			}
		}
		for (AuthorizationRole targetRoleName : roles.getTarget()) {
			final Role targetRole = BasicModel.getRole(identityManager, targetRoleName.getRoleName());
			if (!BasicModel.hasRole(relationshipManager, selectedUser, targetRole)) {
				BasicModel.grantRole(relationshipManager, selectedUser, targetRole);
			}
		}
		uiMessage.addMessage("Role(s) assignation saved for {0} {1}", selectedUser.getFirstName(), selectedUser.getLastName());
		selectedUser = null;
	}

	public List<String> permissionsForRole(){
		List<String> permissions = new ArrayList<>();
		permissions.add("Login");
		permissions.add("View dasboard and general event info");
		for (AuthorizationRole authorizationRole : roles.getTarget()) {
			permissions.addAll(authorizationRole.getPermissions());
		}
		return permissions;
	}

	public User getUser() {
		return user;
	}

	@Restrict("#{hasRole('superUser')}")
	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	@Restrict("#{hasRole('superUser')}")
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	@Restrict("#{hasRole('superUser')}")
	public void setEmail(String email) {
		this.email = email;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	@Restrict("#{hasRole('superUser')}")
	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
		onUserSelection();
	}

	public DualListModel<AuthorizationRole> getRoles() {
		return roles;
	}

	@Restrict("#{hasRole('superUser')}")
	public void setRoles(DualListModel<AuthorizationRole> roles) {
		this.roles = roles;
	}

	public void onTransfer(TransferEvent event) {
		//we don't have to do anything the form in the front-end will be updated automatically
	}
}
