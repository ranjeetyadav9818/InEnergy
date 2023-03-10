package com.inenergis.controller.program;

import com.inenergis.entity.Lse;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.service.IsoProductService;
import com.inenergis.service.IsoResourceService;
import com.inenergis.service.IsoService;
import com.inenergis.service.LseService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class CreateResource implements Serializable{

	@Inject
	IsoResourceService resourceService;
	@Inject
	SubLapService subLapService;
	@Inject
	LseService lseService;
	@Inject
	IsoProductService isoProductService;
	@Inject
	IsoService isoService;
	@Inject
	UIMessage uiMessage;

	Logger log =  LoggerFactory.getLogger(CreateResource.class);

	private IsoResource resource = new IsoResource();
	private List<SubLap> sublaps = new ArrayList<>();
	private List<Lse> lses = new ArrayList<>();
	private List<IsoProduct> isoProducts = new ArrayList<>();
	private Iso iso;

	@PostConstruct
	public void init(){
		iso = ParameterEncoderService.retrieveIsoFromParameter(isoService, uiMessage);
		if (iso != null) {
		    isoProducts = iso.getActiveProfile().getProducts();
		}
		sublaps = subLapService.getAll();
		lses = lseService.getAll();
	}

	public void save(){
		try {
			resourceService.saveResource(resource);
			uiMessage.addMessage("Resource "+resource.getName()+" saved");
		} catch (Exception e) {
			uiMessage.addMessage("Error trying to save resource, please try again later and contact your administrator if you keep having this problem");
			log.warn("Error saving an resource", e);
		}
		resource = new IsoResource();
	}
}
