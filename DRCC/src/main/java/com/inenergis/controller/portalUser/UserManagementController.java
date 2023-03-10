package com.inenergis.controller.portalUser;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.PortalUser;
import com.inenergis.service.PortalUserService;
import com.inenergis.service.ServiceAgreementService;
import lombok.Getter;
import lombok.Setter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by egamas on 05/10/2017.
 */
@Named
@ViewScoped
@Getter
@Setter
public class UserManagementController implements Serializable {

    @Inject
    PortalUserService userService;

    @Inject
    ServiceAgreementService serviceAgreementService;

    private PortalUser newPortalUser;

    private  BaseServiceAgreement serviceAgreement;

    public void saveUser(BaseServiceAgreement selectedServiceAgreement) {
        newPortalUser = userService.saveOrUpdate(newPortalUser);
        selectedServiceAgreement.getPortalUsers().add(newPortalUser);
        newPortalUser = null;
    }

    public void editUser(PortalUser user) {
        userService.saveOrUpdate(user);
    }

    public void cancelNewlUser() {
        newPortalUser = null;
    }

    public void addUser(BaseServiceAgreement selectedServiceAgreement) {
        newPortalUser = new PortalUser();
        newPortalUser.setServiceAgreement(selectedServiceAgreement);
    }

    public void removeUser(PortalUser user, BaseServiceAgreement selectedServiceAgreement){
        selectedServiceAgreement.getPortalUsers().remove(user);
        userService.delete(user);
    }
}
