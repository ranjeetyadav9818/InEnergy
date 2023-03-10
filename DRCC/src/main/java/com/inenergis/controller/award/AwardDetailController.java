package com.inenergis.controller.award;

import com.inenergis.entity.Event;
import com.inenergis.entity.award.Award;
import com.inenergis.entity.award.Instruction;
import com.inenergis.entity.award.Trajectory;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.Segment;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.service.AwardService;
import com.inenergis.service.BidService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class AwardDetailController implements Serializable {

    private String tradeDateFrom; //parameters to return to origin page
    private String tradeDateTo;
    private String resourceName;
    private boolean render = true;
    private Award award;
    private Bid bid;
    List<ScheduleRow> rows;
    List<Event> oneEventList = new ArrayList<>();

    @Inject
    EntityManager entityManager;

    @Inject
    AwardService awardService;

    @Inject
    BidService bidService;

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");

    private String selectedXml;

    @PostConstruct
    public void init() {
        Long awardId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        award = awardService.getById(awardId);
        bid = bidService.searchByResourceIdTradeDateStatus(award.getResource().getId(), award.getTradeDate(), BidStatus.ACCEPTED);
        buildComparisonRows();

        if(award.getEvent() != null){
            oneEventList = Arrays.asList(award.getEvent());
        }

        tradeDateFrom = PrimeFacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("f");
        tradeDateTo = PrimeFacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("t");
        resourceName = PrimeFacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("n");
    }

    private void buildComparisonRows() {
        if (bid != null) {
            ScheduleRow capacitySegment = new ScheduleRow();
            capacitySegment.setScheduleType("Awarded Capacity");
            capacitySegment.setValues(awardService.calculateAwardCapacity(award));
            rows = new ArrayList<>(2 * bid.getSegments().size() + 1);
            rows.add(capacitySegment);
            List<Segment> segments = bid.getSegments();
            if (segments != null && !segments.isEmpty()) {
                for (Segment segment : segments) {

                    ScheduleRow rowPrices = new ScheduleRow();
                    rowPrices.setScheduleType(segment.getName() + ": Price ($/MWH)");
                    rowPrices.setPrice(true);
                    rowPrices.setValues(segment.getPricesAsList());
                    rows.add(rowPrices);

                    ScheduleRow rowCapacities = new ScheduleRow();
                    rowCapacities.setScheduleType(segment.getName() + ": Capacity (MW)");
                    rowCapacities.setPrice(false);
                    rowCapacities.setValues(segment.getCapacitiesAsList());
                    rows.add(rowCapacities);
                }
            }
        }
    }


    public void onBreadCrumbClick() throws IOException {
        String url = "AwardSummary.xhtml?o=" +
                "&f=" + tradeDateFrom + "&t=" + tradeDateTo;
        if (resourceName != null) {
            url += "&n=" + resourceName;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public String getTradeDateFormatted() {
        return TimeUtil.getTradeDateFormatted(award.getTradeDate());
    }

    public void onSelect(SelectEvent e) throws IOException {
        Event event = (Event) e.getObject();
        String redirectionString = "EventDispatchDetail.xhtml?o=" + ParameterEncoderService.encode(event.getId());
        FacesContext.getCurrentInstance().getExternalContext().redirect(redirectionString);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void formatXML(Instruction instruction) {
        selectedXml = instruction.getXmlSource();
    }

    public void formatXML(Trajectory trajectory) {
        selectedXml = trajectory.getXmlSource();
    }

}
