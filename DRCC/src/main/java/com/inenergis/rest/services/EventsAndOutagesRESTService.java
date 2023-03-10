package com.inenergis.rest.services;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.IsoOutage;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.program.ImpactedResource;
import com.inenergis.rest.exception.NonExistingResourceException;
import com.inenergis.rest.model.eventOutage.EventsAndOutagesInput;
import com.inenergis.rest.model.eventOutage.EventsAndOutagesList;
import com.inenergis.rest.model.eventOutage.EventsAndOutagesOutput;
import com.inenergis.rest.model.eventOutage.EventsAndOutagesRequest;
import com.inenergis.rest.model.eventOutage.EventsAndOutagesResponse;
import com.inenergis.service.IsoResourceService;
import com.inenergis.util.TimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
public class EventsAndOutagesRESTService {

    @Inject
    IsoResourceService isoResourceService;

    public EventsAndOutagesResponse getEventsAndOutages(EventsAndOutagesRequest input) {
        EventsAndOutagesResponse result = new EventsAndOutagesResponse();
        final EventsAndOutagesList eventsAndOutagesResponse = new EventsAndOutagesList();
        eventsAndOutagesResponse.setListOfEventOrOutageDays(new ArrayList<>());
        result.setEventsAndOutagesResponse(eventsAndOutagesResponse);
        EventsAndOutagesInput request = input.getEventsAndOutageDaysRequest();
        List<IsoResource> resources = isoResourceService.getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("name")
                .value(request.getResourceId()).matchMode(MatchMode.EXACT).build()));
        if (CollectionUtils.isEmpty(resources)) {
            throw new NonExistingResourceException("There is no resource in the system with id " + request.getResourceId());
        }
        for (IsoResource resource : resources) {
            for (IsoOutage isoOutage : resource.getOutages()) {
                if (TimeUtil.between(request.getStartDate(), request.getEndDate(), isoOutage.getDate())) {
                    result.getEventsAndOutagesResponse().getListOfEventOrOutageDays().add(createEventOrOutage(false, isoOutage.getDate()));
                }
            }
            for (ImpactedResource impactedResource : resource.getImpactedResources()) {
                if (TimeUtil.between(request.getStartDate(), request.getEndDate(), impactedResource.getEvent().getStartDate(), impactedResource.getEvent().getEndDate())) {
                    result.getEventsAndOutagesResponse().getListOfEventOrOutageDays().add(createEventOrOutage(true, impactedResource.getEvent().getStartDate()));
                }

            }
        }
        return result;
    }

    private EventsAndOutagesOutput createEventOrOutage(boolean b, Date date) {
        EventsAndOutagesOutput output = new EventsAndOutagesOutput();
        output.setEvent(b);
        output.setEventOrOutageDate(date);
        return output;
    }
}
