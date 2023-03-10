package com.inenergis.rest.model.eventOutage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventsAndOutagesRequest {

    @JsonProperty("EventsAndOutageDaysRequest")
    private EventsAndOutagesInput eventsAndOutageDaysRequest;

}
