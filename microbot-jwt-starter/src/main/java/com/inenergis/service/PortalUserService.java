package com.inenergis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inenergis.dao.PaymentDao;
import com.inenergis.dao.PortalUserDao;
import com.inenergis.entity.PortalUser;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.Payment;
import com.inenergis.model.adapter.InvoiceAdapter;
import com.inenergis.model.adapter.PaymentAdapter;
import com.inenergis.model.adapter.PortalServiceAgreementAdapter;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.inenergis.entity.billing.Invoice.InvoiceStatus.FINAL;

/**
 * Created by egamas on 22/09/2017.
 */
@Getter
@Setter
@Component
public class PortalUserService {

    private static final Logger log = LoggerFactory.getLogger(PortalUserService.class);

    @Autowired
    private PortalUserDao portalUserDao;

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    ObjectMapper objectMapper;

    @Transactional("mysqlTransactionManager")
    public PortalUser getByEmail(String email) {
        final PortalUser byEmail = portalUserDao.findByEmail(email);
        return byEmail;
    }


    @Transactional("mysqlTransactionManager")
    public String getServiceAgreementByEmail(String email) throws JsonProcessingException {
        final ServiceAgreement serviceAgreement = getServiceAgreement(email);
        return objectMapper.writeValueAsString(PortalServiceAgreementAdapter.build(serviceAgreement));
    }

    @Transactional("mysqlTransactionManager")
    public String getInvoicesByEmail(String email) throws JsonProcessingException {
        final ServiceAgreement serviceAgreement = getServiceAgreement(email);
        return objectMapper.writeValueAsString(InvoiceAdapter.buildInvoices(serviceAgreement));
    }

    public ServiceAgreement getServiceAgreement(String email) {
        final PortalUser byEmail = portalUserDao.findByEmail(email);
        return byEmail.getServiceAgreement();
    }

    @Transactional("mysqlTransactionManager")
    public Long getBalanceByEmail(String email) throws JsonProcessingException {
        final ServiceAgreement serviceAgreement = getServiceAgreement(email);

        Long debitTotal = serviceAgreement.getInvoices().stream()
                .filter(invoice -> invoice.getStatus().equals(FINAL))
                .mapToLong(Invoice::getTotal)
                .sum();
        Long creditTotal = serviceAgreement.getPayments().stream()
                .mapToLong(Payment::getValue)
                .sum();
        return debitTotal - creditTotal;
    }

    @Transactional("mysqlTransactionManager")
    public String getPaymentsByEmail(String email) throws JsonProcessingException {
        final ServiceAgreement serviceAgreement = getServiceAgreement(email);
        return objectMapper.writeValueAsString(PaymentAdapter.buildPayments(serviceAgreement));
    }

    @Transactional("mysqlTransactionManager")
    public List<ServicePoint> getServicePointsByEmail(String email) {
        final ServiceAgreement serviceAgreement = getServiceAgreement(email);
        return serviceAgreement.getAgreementPointMaps().stream().map(apm -> apm.getServicePoint()).collect(Collectors.toList());
    }

    @Transactional("mysqlTransactionManager")
    public void save(PortalUser user) {
        portalUserDao.save(user);
    }

    @Transactional("mysqlTransactionManager")
    public String getLastPayment(String email) throws JsonProcessingException {
        final ServiceAgreement serviceAgreement = getServiceAgreement(email);
        final Payment payment = paymentDao.getFirstByServiceAgreementServiceAgreementIdOrderByDateDesc(serviceAgreement.getServiceAgreementId());
        return objectMapper.writeValueAsString(PaymentAdapter.build(payment));
    }

    @Transactional("mysqlTransactionManager")
    public String getRatePlans(String email) throws JsonProcessingException {
        final ServiceAgreement serviceAgreement = getServiceAgreement(email);
        return objectMapper.writeValueAsString(PortalServiceAgreementAdapter.buildRatePlans(serviceAgreement));
    }

}
