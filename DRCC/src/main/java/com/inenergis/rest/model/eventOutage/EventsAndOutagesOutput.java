package com.inenergis.rest.model.eventOutage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class EventsAndOutagesOutput {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date eventOrOutageDate;
    private boolean event;

}
