package com.inenergis.controller.map;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.SubLapPolygonCoordinates;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Named
@ViewScoped
@Getter
@Setter
public class MapController implements Serializable{

	private MapModel model;
	private Program program;
	private Program program2;
	private Marker marker;

	@Inject
    ProgramServiceContract programService;
	@Inject
    SubLapService subLapService;
	@Inject
    UIMessage uiMessage;

	@PostConstruct
	public void init() {
		model = new DefaultMapModel();
	}

    public void search() {
	    if(program!=null){
	        model = new DefaultMapModel();
            List<ProgramServiceAgreementEnrollment> activeSAs = programService.getActiveSAs(program);
            for (ProgramServiceAgreementEnrollment enrollment : activeSAs) {
                ServicePoint servicePoint = (ServicePoint) enrollment.getServiceAgreement().getAgreementPointMaps().get(0).getServicePoint();
                LatLng coord1 = new LatLng(new Double(servicePoint.getLatitude()), new Double(servicePoint.getLongitude()));
                Marker overlay = new Marker(coord1, enrollment.getServiceAgreement().getServiceAgreementId(), enrollment);
                model.addOverlay(overlay);
            }
        }
    }

    public void showRisks(){
        if(program2!=null){
            model = new DefaultMapModel();
            List<ProgramServiceAgreementEnrollment> activeSAs = programService.getActiveSAs(program2);
            Random random = new Random();
            for (ProgramServiceAgreementEnrollment enrollment : activeSAs) {
                ServicePoint servicePoint = (ServicePoint) enrollment.getServiceAgreement().getAgreementPointMaps().get(0).getServicePoint();
                LatLng coord1 = new LatLng(new Double(servicePoint.getLatitude()), new Double(servicePoint.getLongitude()));
                if (random.nextBoolean()) {
                    Marker overlay = new Marker(coord1, enrollment.getServiceAgreement().getServiceAgreementId(), enrollment,"http://maps.google.com/mapfiles/ms/micons/blue-dot.png");
                    model.addOverlay(overlay);
                }else{
                    Marker overlay = new Marker(coord1, enrollment.getServiceAgreement().getServiceAgreementId(), enrollment);
                    model.addOverlay(overlay);
                }
            }
        }
    }

    public void showTemperature(){
        model = new DefaultMapModel();
        LatLng coord1 = new LatLng(37.123984, -122.125643);
        LatLng coord2 = new LatLng(38.123984, -122.125643);
        Circle circle1 = new Circle(coord1, 30000);
        circle1.setStrokeColor("#d93c3c");
        circle1.setFillColor("#d93c3c");
        circle1.setFillOpacity(0.5);
        circle1.setData("South station");

        Circle circle2 = new Circle(coord2, 22000);
        circle2.setStrokeColor("#00ff00");
        circle2.setFillColor("#00ff00");
        circle2.setStrokeOpacity(0.7);
        circle2.setFillOpacity(0.7);
        circle2.setData("North station");


        model.addOverlay(circle1);
        model.addOverlay(circle2);
    }

    public void showSublaps(){
        for (SubLap sublap : subLapService.getAll()) {
            if(CollectionUtils.isNotEmpty(sublap.getCoordinates())){
                model.addOverlay(createPolyon(sublap));
            }
        }
    }

    private Overlay createPolyon(SubLap sublap) {
        Polygon polygon = new Polygon();
        polygon.setStrokeColor("#FF9900");
        polygon.setFillColor("#FF9900");
        polygon.setStrokeOpacity(0.7);
        polygon.setFillOpacity(0.3);
        polygon.setData(sublap);
        List<SubLapPolygonCoordinates> coordinates = sublap.getCoordinates();
        Collections.sort(coordinates, Comparator.comparingInt(SubLapPolygonCoordinates::getOrder));
        for (SubLapPolygonCoordinates coordinate : coordinates) {
            LatLng coord = new LatLng(new Double(coordinate.getLatitude().doubleValue()), new Double(coordinate.getLongitude().doubleValue()));
            polygon.getPaths().add(coord);
        }
        return polygon;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        if(event.getOverlay() instanceof  Marker){
            marker = (Marker) event.getOverlay();
        }else{
            marker = null;
        }
        if(event.getOverlay() instanceof Polygon){
            uiMessage.addMessage("Sublap {0} selected, 129 active locations in the area", ((SubLap) event.getOverlay().getData()).getName());
        }
        if(event.getOverlay() instanceof Circle){
            uiMessage.addMessage("{0}, 95°F", event.getOverlay().getData());
        }
    }

	public List<Program> getPrograms(){
		return programService.getPrograms();
	}

}