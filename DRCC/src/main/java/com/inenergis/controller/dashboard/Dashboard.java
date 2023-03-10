package com.inenergis.controller.dashboard;

import com.inenergis.controller.authentication.AuthorizationChecker;
import com.inenergis.controller.events.EventList;
import com.inenergis.entity.PdpSrEvent;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.BidHelper;
import com.inenergis.entity.bidding.BidProperties;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.service.BidService;
import com.inenergis.service.ParameterEncoderService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import javax.annotation.PostConstruct;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Transactional
@Getter
@Setter
public class Dashboard implements Serializable{

	@Inject
	EntityManager entityManager;
	
	@Inject
	EventList eventList;
	
	@Inject
	AuthorizationChecker authorizationChecker;

    @Inject
    BidService bidService;
	 
	Logger log =  LoggerFactory.getLogger(Dashboard.class);
	
	private PdpSrEvent pdpSrEvent;
	private List<PdpSrEvent> pdpSrEvents = new ArrayList<PdpSrEvent>();
    private DashboardModel model;
    private ScheduleModel scheduleModel;
    private List<Bid> bids;

    private List<String> deletedPanels = new ArrayList<>();
    private String selectedPanel;
	
	@PostConstruct
	public void onCreate(){
		this.loadEvents();
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();

        column1.addWidget("most_recent_events");
        column1.addWidget("my_programs");
        column1.addWidget("alerts");
        column1.addWidget("upcoming_events");

        column2.addWidget("next_bids");
        column2.addWidget("current_event");
        column2.addWidget("events_summary");


        model.addColumn(column1);
        model.addColumn(column2);
        bids = mockBids();

        scheduleModel = new DefaultScheduleModel();
        scheduleModel.addEvent(new DefaultScheduleEvent("Event 12300", generateDate(5,6), generateDate(5,11)));
        scheduleModel.addEvent(new DefaultScheduleEvent("Event 12301", generateDate(3,4), generateDate(3,5)));
        scheduleModel.addEvent(new DefaultScheduleEvent("Event 12302", generateDate(1,8), generateDate(1,11)));

    }

    private Date generateDate(int daysInThePast, int hour) {
        Calendar t = (Calendar) GregorianCalendar.getInstance().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - daysInThePast);
        t.set(Calendar.HOUR, hour);

        return t.getTime();
    }

    private List<Bid> mockBids() {
        List<Bid> bids = bidService.getAll().stream().map(s -> s.getBid()).limit(5).collect(Collectors.toList());
        for (Bid bid : bids) {
//            BidProperties properties = new BidProperties(bid.getIsoResource().getActiveRegistration());
            BidHelper.assignRisks(bid, new ArrayList<>());
        }
        return bids;
    }

    public void loadEvents(){
		List<PdpSrEvent> events = entityManager.createQuery("SELECT e FROM PdpSrEvent e ORDER BY e.eventStart DESC").setMaxResults(5).getResultList();
		if(!events.isEmpty()){
			pdpSrEvents = events;
		}
	}
	
	public void onRowSelectEvent(SelectEvent event) throws IOException {
		this.setPdpSrEvent((PdpSrEvent)event.getObject());
		this.handleDashboardRedirect(this.pdpSrEvent);
    }
	
	public void handleDashboardRedirect(PdpSrEvent event) throws IOException{
		FacesContext.getCurrentInstance().getExternalContext().redirect("EventList.xhtml?o=" + ParameterEncoderService.encode(event.getEventId()));
	    FacesContext.getCurrentInstance().responseComplete();
	}

    public void handleReorder(DashboardReorderEvent event) {
//        FacesMessage message = new FacesMessage();
//        message.setSeverity(FacesMessage.SEVERITY_INFO);
//        message.setSummary("Reordered: " + event.getWidgetId());
//        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());
//        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void handleClose(CloseEvent event) {
        String id = event.getComponent().getId();
        deletedPanels.add(id);
        for (DashboardColumn dashboardColumn : model.getColumns()) {
            if (dashboardColumn.getWidgets().contains(id)) {
                dashboardColumn.removeWidget(id);
            }
        }
    }

    public void addPanel(){
        if(selectedPanel!=null){
            model.getColumn(1).addWidget(selectedPanel);
            deletedPanels.remove(selectedPanel);
        }
    }
	
}
