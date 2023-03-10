package com.inenergis.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.event.RowEditEvent;

import com.inenergis.entity.PdpSrNotification;
import com.inenergis.entity.PdpSrVendor;
import com.inenergis.entity.VendorStatusMapping;
import com.inenergis.util.UIMessage;

@Named
@ViewScoped
@Transactional
public class VendorStatusMappingList implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Inject
	EntityManager entityManager;
	
	@Inject
    private FacesContext facesContext;
	
	@Inject 
	UIMessage uiMessage;
	 
	Logger log =  LoggerFactory.getLogger(VendorStatusMappingList.class);
	
	private VendorStatusMapping vendorStatusMapping;
	
	private VendorStatusMapping selectedVendorStatusMapping;
	
	private List<VendorStatusMapping> vendorStatusMappings = new ArrayList<VendorStatusMapping>();
	
	private List<PdpSrVendor> vendors = new ArrayList<PdpSrVendor>();
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void onCreate(){
		this.vendorStatusMappings = entityManager.createNamedQuery("VendorStatusMapping.findAll").getResultList();
		this.vendors = entityManager.createNamedQuery("PdpSrVendor.findAll").getResultList();
	}
	
	public void add(){
		this.vendorStatusMapping = new VendorStatusMapping();
	}
	
	public void saveVendorStatusMapping(){
		List<VendorStatusMapping> vsms = entityManager.createQuery("SELECT v FROM VendorStatusMapping v " +
				"WHERE v.vendor = :vendor " +
				"AND v.statusCode = :statusCode")
				.setParameter("vendor", this.vendorStatusMapping.getVendor())
				.setParameter("statusCode", this.vendorStatusMapping.getStatusCode())
				.getResultList();
		// only save if there is not a duplicate
		if(vsms == null || vsms.isEmpty()){
			entityManager.persist(this.vendorStatusMapping);
			entityManager.flush();
			this.vendorStatusMappings.add(0, this.vendorStatusMapping);
			this.vendorStatusMapping = null;
		} else {
			// we need to throw a proper exception here
			facesContext.addMessage(null, new FacesMessage("The status code already exists for this vendor. Duplicate mappings of vendor status codes is not permitted."));
            return;
		}
//		this.onCreate();
	}
	
	public void cancelVendorStatusMapping(){
		this.vendorStatusMapping = null;
	}
	
	@Transactional
	public void deleteVendorStatusMapping(){
		if(this.selectedVendorStatusMapping!=null){
			this.vendorStatusMappings.remove(this.selectedVendorStatusMapping);
			if(!entityManager.contains(this.selectedVendorStatusMapping)){
				this.selectedVendorStatusMapping = entityManager.find(VendorStatusMapping.class, this.selectedVendorStatusMapping.getVendorStatusMappingId());
			}
			if(entityManager.contains(this.selectedVendorStatusMapping)){
				entityManager.remove(this.selectedVendorStatusMapping);
				this.selectedVendorStatusMapping = null;
				this.vendorStatusMapping = null;
				entityManager.flush();
			}
		}
	}

	public void onRowEdit(RowEditEvent event) {		
		VendorStatusMapping u = (VendorStatusMapping)event.getObject();
		VendorStatusMapping v = (VendorStatusMapping) entityManager.find(VendorStatusMapping.class, u.getVendorStatusMappingId());
		v.setStatusCode(u.getStatusCode());
		v.setDisplayMessage(u.getDisplayMessage());
		v.setVendorMessage(u.getVendorMessage());
		v.setSuccessfulNotification(u.getSuccessfulNotification());
		entityManager.flush();
    }
     
    public void onRowCancel(RowEditEvent event) {
    	this.vendorStatusMapping = (VendorStatusMapping)event.getObject();
    }

	public VendorStatusMapping getVendorStatusMapping() {
		return vendorStatusMapping;
	}

	public void setVendorStatusMapping(VendorStatusMapping vendorStatusMapping) {
		this.vendorStatusMapping = vendorStatusMapping;
	}

	public List<VendorStatusMapping> getVendorStatusMappings() {
		return vendorStatusMappings;
	}

	public void setVendorStatusMappings(List<VendorStatusMapping> vendorStatusMappings) {
		this.vendorStatusMappings = vendorStatusMappings;
	}

	public List<PdpSrVendor> getVendors() {
		return vendors;
	}

	public void setVendors(List<PdpSrVendor> vendors) {
		this.vendors = vendors;
	}

	public VendorStatusMapping getSelectedVendorStatusMapping() {
		return selectedVendorStatusMapping;
	}

	public void setSelectedVendorStatusMapping(VendorStatusMapping selectedVendorStatusMapping) {
		this.selectedVendorStatusMapping = selectedVendorStatusMapping;
	}
	
}
