package com.inenergis.billingEngine;

import com.inenergis.billingEngine.service.BillingCycleService;
import com.inenergis.billingEngine.service.InvoiceService;
import com.inenergis.billingEngine.service.ServiceAgreementService;
import com.inenergis.entity.BillingCycleSchedule;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.util.ConstantsProviderModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

//TODO move all of this to one shot
@Component
public class ScheduledTasks {

    private static final int BATCH_SIZE = 10_000;

    @Autowired
    private JmsTemplate defaultJmsTemplate;

    @Autowired
    private BillingCycleService billingCycleService;

    @Autowired
    private ServiceAgreementService serviceAgreementService;

    @Autowired
    private InvoiceService invoiceService;

    @Value("${jms.queue.name}")
    private String ratePlanDueQueueName;

    @Value("${jms.queue.retryInvoice.name}")
    private String invoicesToRetryQueueName;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(fixedDelay = 10_000) // TODO Every 5 minutes
    @Transactional
    public void checkForInvoicesToretry() throws IOException {
        for (Invoice invoice : invoiceService.getInvoicesByStatus(Invoice.InvoiceStatus.RETRYING)) {
            invoice.setStatus(Invoice.InvoiceStatus.GENERATING);
            invoiceService.saveInvoice(invoice);
            defaultJmsTemplate.convertAndSend( invoicesToRetryQueueName, invoice.getId());
            log.info("invoice {} is going to be regenerated", invoice.getId());
        }
    }

    @Scheduled(fixedDelay = 10_000) // TODO Every hour
    public void reportCurrentTime() {
        discoverServiceAgreements();
        log.info("Exit");
    }

    public synchronized void discoverServiceAgreements(){
        log.info("Entry point "+ LocalDateTime.now());
        BillingCycleSchedule cycle = billingCycleService.findByDate(LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
        log.info("cycle "+ cycle);
        if (cycle != null && !cycle.isSent()) {
            int i = 0;
            while (sendSAToQueue(cycle, i, BATCH_SIZE)) {
                log.info("batch {} from cycle {} sent on {}", i * BATCH_SIZE, cycle, new Date());
                i++;
            }
            cycle.setSent(true);
            billingCycleService.update(cycle);
            log.info("all SAs in {} are sent", cycle);
        }
    }

    private boolean sendSAToQueue(BillingCycleSchedule cycle, int i, int batchSize) {
        List<BaseServiceAgreement> list = serviceAgreementService.getServiceAgreementByBillingCycle(cycle.getSerial(), new PageRequest(i, batchSize));
        if (CollectionUtils.isNotEmpty(list)) {
            list.parallelStream().forEach(serviceAgreement ->
                    defaultJmsTemplate.convertAndSend( ratePlanDueQueueName, serviceAgreement.getServiceAgreementId()));
            return true;
        }
        return false;
    }
}