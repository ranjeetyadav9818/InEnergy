package com.inenergis.microbot.camel.processors;


import com.inenergis.microbot.camel.csv.CustomerInterface;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

public class AssignCustomerToHeaderProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        List<CustomerInterface> customersForUnenrolling = (List<CustomerInterface>) exchange.getIn().getHeader("customersForUnenrolling");
        customersForUnenrolling.add(((CustomerInterface) exchange.getIn().getBody()));
    }
}
