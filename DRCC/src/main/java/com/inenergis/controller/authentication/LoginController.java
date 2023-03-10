package com.inenergis.controller.authentication;

import com.inenergis.service.MailService;
import com.inenergis.util.PropertyAccessor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;
import org.picketlink.authentication.CredentialExpiredException;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.User;
import org.primefaces.context.PrimeFacesContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


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
public class LoginController implements Serializable{

    public static final String LOST_PASSWORD_BODY = "templates/lost-password-body.vm";
    public static final String LOST_PASSWORD_HEADER = "templates/lost-password-header.vm";
    @Inject
    private Identity identity;

    @Inject
    private PartitionManager partitionManager;

    @Inject
    private FacesContext facesContext;

    @Inject
    private DefaultLoginCredentials loginCredentials;

    @Inject
    private Logger log;

    @Inject
    private MailService mailService;

    @Inject
    PropertyAccessor propertyAccessor;

    private boolean reset;
    private String email;
    private boolean firstAccess;
    private String newPassword1;
    private String newPassword2;
    private String userIdFirstAccess;

    public String login() {
    	if(!identity.isLoggedIn()) {
            try {
                AuthenticationResult result = identity.login();
                if (AuthenticationResult.FAILED.equals(result)) {
                    facesContext.addMessage(null, new FacesMessage("Authentication unsuccessful. Please check your username and password before trying again."));
                    return null;
                }
            } catch (CredentialExpiredException e) {
                firstAccess = true;
                userIdFirstAccess = loginCredentials.getUserId();
                return null;
            }
        }
        return "dashboard?faces-redirect=true";
    }

    public String logout(){
    	identity.logout();
        PrimeFacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    	return "login?faces-redirect=true";
    }

    public void changePassword(){
        if(newPassword1.equalsIgnoreCase(newPassword2)){
            firstAccess = false;
            final IdentityManager identityManager = partitionManager.createIdentityManager();
            identityManager.updateCredential(BasicModel.getUser(identityManager, userIdFirstAccess),new Password(newPassword1));
            facesContext.addMessage(null, new FacesMessage("Your password has been changed. Plese use it for login"));
        }else{
            facesContext.addMessage(null, new FacesMessage("Passwords are not identical."));
        }
    }

    public void resetMyPassword(){
        final IdentityManager identityManager = partitionManager.createIdentityManager();
        User user = BasicModel.getUser(identityManager, this.getEmail());
        if(user == null){
             facesContext.addMessage(null, new FacesMessage("Sorry, we can't find that email in our system"));
        }else{
            final String password = UUID.randomUUID().toString();
            String applicationUrl = propertyAccessor.getValue("drcc.url");

            user.setAttribute(new Attribute<>("password_recovery", password));
            identityManager.update(user);

            final boolean mailSent = mailService.sendTemplate(this.getEmail(), propertyAccessor.getValue("mail.user"),LOST_PASSWORD_BODY, LOST_PASSWORD_HEADER, getEmailValues(user, applicationUrl, this.getEmail(), password));
            if (mailSent) {
                email = null;
                facesContext.addMessage(null, new FacesMessage("Mail sent, please review your junk folder if you haven't received any email in the next minutes."));
            } else {
                facesContext.addMessage(null, new FacesMessage("Mail not sent, please try again."));
            }
        }
    }

    private Map<String, Object> getEmailValues(User user, String applicationUrl, String email, String password) {
        Map<String, Object> values =new HashMap<>();
        values.put("user",user);
        values.put("applicationUrl",applicationUrl);
        values.put("email",email);
        values.put("password",password);
        return values;
    }

    public void closeReset(){
        reset = false;
    }

    public void openReset(){
        reset = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isFirstAccess() {
        return firstAccess;
    }

    public void setFirstAccess(boolean firstAccess) {
        this.firstAccess = firstAccess;
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