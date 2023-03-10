package com.inenergis.billingEngine;

//import com.inenergis.billingEngine.service.BaseServicePoint;
import com.inenergis.billingEngine.service.InvoiceService;
import com.inenergis.billingEngine.service.MeterService;
import com.inenergis.billingEngine.service.RatePlanService;
import com.inenergis.billingEngine.service.ServiceAgreementService;
import com.inenergis.billingEngine.service.ServicePointService;
import com.inenergis.billingEngine.service.billing.BillingService;
import com.google.gson.Gson;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.BaseServicePoint;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.meterData.IntervalData;
import com.inenergis.entity.meterData.PeakDemandMeterData;
import com.inenergis.entity.program.RatePlan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HelloController {
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private JmsTemplate defaultJmsTemplate;
    @Autowired
    private ServicePointService servicePointService;
    @Autowired
    private MeterService meterService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private RatePlanService ratePlanService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private ServiceAgreementService serviceAgreementService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/process")
    @Transactional("mysqlTransactionManager")
    public String process() {
        log.info("Request done: "+ LocalDateTime.now());

        //AgreementPointMap agreementPointMap = agreementPointMapService.getFirstByServicePointId("0000599410");
        //List<RateProfileConsumptionFee> consumptionFees = agreementPointMap.getServiceAgreement().getRatePlanEnrollments().get(0).getRatePlan().getActiveProfile().getActiveConsumptionFees();

        String serviceAgrementId = "6814256072";
        LocalDate of = LocalDate.of(2017, 1, 1);
        LocalDate to = LocalDate.of(2017, 1, 30);
        BaseServiceAgreement serviceAgreement = serviceAgreementService.getServiceAgreement(serviceAgrementId);
        if (serviceAgreement != null) {
            Invoice invoice = invoiceService.getInvoiceByDate(of, to, serviceAgreement);
            invoice.setDateFrom(of);
            invoice.setDateTo(to);
            try {
                billingService.generateInvoice(serviceAgrementId, invoice);
            } catch (IOException e) {
                return "Error procesing meter data "+e.getMessage();
            }
            return "Processed meter data. Invoice id "+invoice.getId() + "uuid "+invoice.getUuid();
        } else {
            return "Service agreement does not have service point associated";
        }
    }

    @RequestMapping("/ratePlansDue")
    @Transactional("mysqlTransactionManager")
    public String run() {
        Set<RatePlan> ratePlans = ratePlanService.findAllWithDueDate(LocalDate.now());

        return ratePlans.stream()
                .map(ratePlan -> ratePlan.getId().toString())
                .collect(Collectors.joining(","));
    }

    @RequestMapping("sendMessage")
    public String send() {
        defaultJmsTemplate.convertAndSend("EA-MeterData-Test3", "com/inenergis");
        return "Message sent";
    }

    @RequestMapping("aurora")
    public String testAurora() {
        BaseServicePoint servicePoint = servicePointService.findByServicePointId("9446624789");
        return "id: " + servicePoint.getServicePointId();
    }

    @RequestMapping(value = "meter/sp/{id}", method = GET)
    public String meter(@PathVariable("id") long id) {
        List<IntervalData> meterData = meterService.findAllByServicePointId(Long.toString(id));
        return meterData.stream().map(IntervalData::getValue).reduce(BigDecimal.ZERO, BigDecimal::add).toString();
    }

    @RequestMapping(value = "meter/peak/sp/{id}", method = GET)
    public String peakMeter(@PathVariable("id") long id) {
        List<PeakDemandMeterData> meterData = meterService.findAllPeakByServicePointId(Long.toString(id));
        return meterData.stream().map(PeakDemandMeterData::getValue).reduce(BigDecimal.ZERO, BigDecimal::add).toString();
    }

    @RequestMapping(value = "meter/sp/{id}/{time}", method = GET)
    public String meter(@PathVariable("id") long id, @PathVariable("time") String time) {
        Map<Long, BigDecimal> hourlyData = meterService.getHourlyMeterData(id, time);

        Gson gson = new Gson();
        return gson.toJson(hourlyData);
    }

}