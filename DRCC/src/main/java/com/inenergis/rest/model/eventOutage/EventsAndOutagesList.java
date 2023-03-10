package com.inenergis.rest.model.eventOutage;

import lombok.Data;

import java.util.List;

@Data
public class EventsAndOutagesList {

    private List<EventsAndOutagesOutput> listOfEventOrOutageDays;

}
