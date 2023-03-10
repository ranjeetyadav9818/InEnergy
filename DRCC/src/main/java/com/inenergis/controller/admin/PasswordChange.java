package com.inenergis.controller.admin;

import com.inenergis.util.UIMessage;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.picketlink.Identity;
import org.picketlink.authorization.annotations.Restrict;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.Agent;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class PasswordChange implements Serializable{

	Logger log =  LoggerFactory.getLogger(PasswordChange.class);

	@Inject
	EntityManager entityManager;
	@Inject
	Identity identity;
	private IdentityManager identityManager;
	@Inject
	private PartitionManager partitionManager;
	@Inject
	FacesContext facesContext;

	private String currentPassword;
	private String newPassword1;
	private String newPassword2;

	@PostConstruct
	public void onCreate(){
		identityManager = this.partitionManager.createIdentityManager();
	}

	public void changePassword() throws IOException {
		final String loginName = ((User) identity.getAccount()).getLoginName();
		Credentials credentials = new UsernamePasswordCredentials(loginName,new Password(currentPassword));
		identityManager.validateCredentials(credentials);
		if(credentials.getStatus() == Credentials.Status.VALID || credentials.getStatus() == Credentials.Status.EXPIRED){
			if(newPassword1.equalsIgnoreCase(newPassword2)){
				identityManager.updateCredential(identity.getAccount(),new Password(newPassword1));
				facesContext.addMessage("null", new FacesMessage("Your password has been changed"));
				facesContext.getExternalContext().getFlash().setKeepMessages(true);
				facesContext.getExternalContext().redirect("dashboard.xhtml");
			}else{
				facesContext.addMessage("null", new FacesMessage("Passwords are not identical"));
			}
		}else{
			facesContext.addMessage("null", new FacesMessage("The password you have provided is not your current password. If you have forgotten it, please use the reset password link in login page."));
		}
	}
	
	public String cancel(){
		return "dashboard.jsf";
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}
}