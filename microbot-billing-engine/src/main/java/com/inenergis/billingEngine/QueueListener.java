package com.inenergis.billingEngine;

import com.inenergis.billingEngine.service.InvoiceService;
import com.inenergis.billingEngine.service.MeterService;
import com.inenergis.billingEngine.service.ServiceAgreementService;
import com.inenergis.billingEngine.service.billing.BillingService;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.meterData.LatestMeterData;
import com.inenergis.billingEngine.sa.MeterDataUsage;
import com.inenergis.util.ConstantsProviderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class QueueListener {
    private static final Logger log = LoggerFactory.getLogger(QueueListener.class);

    @Autowired
    private MeterService meterService;

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private BillingService billingService;
    @Autowired
    private ServiceAgreementService serviceAgreementService;

    @JmsListener(destination = "EA-NewMeterData2")
    public void test(String requestJSON) throws JMSException {
        log.info(requestJSON);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        List<LatestMeterData> latestMeterDataRedShift = meterService.getLatestMeterData();

        for (LatestMeterData latestMeterDatumRedShift : latestMeterDataRedShift) {
            LocalDate ld = LocalDate.parse(latestMeterDatumRedShift.getTime(), formatter);

            MeterDataUsage meterDataUsage = meterService.getLatestMeterDataUsage(latestMeterDatumRedShift.getServicePointId());
            Map<Long, BigDecimal> meterDataRedShift;
            if (meterDataUsage == null) {
                meterDataRedShift = meterService.getDailyMeterData(latestMeterDatumRedShift.getServicePointId(), "0");
            } else {
                LocalDateTime ldt = meterDataUsage.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (ldt != null && (ld.isBefore(ldt.toLocalDate()) || ld.equals(ldt.toLocalDate()))) {
                    log.info("Service point skipped ");
                    continue;
                }
                meterDataRedShift = meterService.getDailyMeterData(latestMeterDatumRedShift.getServicePointId(), ldt.format(formatter));
            }

            for (Map.Entry<Long, BigDecimal> entry : meterDataRedShift.entrySet()) {
                MeterDataUsage meterDataUsage1 = new MeterDataUsage();
                meterDataUsage1.setDate(Date.from(LocalDate.parse(entry.getKey().toString(), formatter).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                meterDataUsage1.setConsumptionWatts((entry.getValue().multiply(BigDecimal.valueOf(1000L)).longValue()));
                meterDataUsage1.setServicePointId(latestMeterDatumRedShift.getServicePointId());
                meterDataUsage1.setPrice(entry.getValue().multiply(new BigDecimal(2)));
                meterService.saveUsage(meterDataUsage1);
            }
        }
        log.info("Finished");
    }


    @JmsListener(destination = "${jms.queue.name}")
    public void serviceAgreementReceived(String serviceAgrementId) throws JMSException, IOException {
        log.info("service agreement received {}",serviceAgrementId);
        LocalDate to = LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).minusDays(1);
        LocalDate from = LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).minusMonths(1); //TODO proper looking on previous invoice dateTo
        BaseServiceAgreement serviceAgreement = serviceAgreementService.getServiceAgreement(serviceAgrementId);
        if (serviceAgreement != null) {
            Invoice invoice = invoiceService.getInvoiceByDate(from, to, serviceAgreement);
            billingService.generateInvoice(serviceAgrementId, invoice);
        }else{
            log.error("service agreement {} does not exist", serviceAgrementId);
        }
    }

    @JmsListener(destination = "${jms.queue.retryInvoice.name}")
    public void retryInvoice(Long invoiceId) throws IOException {
        log.info("invoice received {}",invoiceId);
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        if (invoice != null) {
            billingService.generateInvoice(invoice.getServiceAgreement().getServiceAgreementId(), invoice);
        }else{
            log.error("invoice {} does not exist", invoiceId);
        }
    }
}