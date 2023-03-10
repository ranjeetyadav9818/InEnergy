package com.inenergis.controller.authentication;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;

/**
 * We control the authentication process from this bean, so that in the event of a failed authentication we can add an
 * appropriate FacesMessage to the response.
 * 
 * @author Immo Benjes
 * 
 */
@Named
@ViewScoped
@Transactional
public class ResetPasswordController implements Serializable{
    
    @Inject
    private PartitionManager partitionManager;

    @Inject
    private FacesContext facesContext;
    
    @Inject
    private Logger log;


    private String newPassword;
    private String repeatNewPassword;
    private String email;
    private String otp;



    @PostConstruct
    public void init(){
        email = facesContext.getExternalContext().getRequestParameterMap().get("email");
        otp = facesContext.getExternalContext().getRequestParameterMap().get("otp");
    }

    public void changePassword() throws IOException {
        final IdentityManager identityManager = partitionManager.createIdentityManager();
        if(!newPassword.equals(repeatNewPassword)){
            facesContext.addMessage(null, new FacesMessage("Sorry, the passwords don't match"));
            return;
        }
        User user = BasicModel.getUser(identityManager, email);
        if(user == null){
            facesContext.addMessage(null, new FacesMessage("Sorry, we can't find that email in our system"));
        }else{
            final Attribute<String> otpFromUser = user.getAttribute("password_recovery");
            if(otpFromUser != null && otpFromUser.getValue().equals(otp)){
                user.removeAttribute("password_recovery");
                identityManager.update(user);
                identityManager.updateCredential(user,new Password(newPassword));
                facesContext.addMessage(null, new FacesMessage("Your password has been changed, please login with your new credentials."));
                facesContext.getExternalContext().getFlash().setKeepMessages(true);
                facesContext.getExternalContext().redirect("login.xhtml?passwordChanged=true");
            }else{
                facesContext.addMessage(null, new FacesMessage("It was not possible to reset your password"));
            }
        }
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }
}