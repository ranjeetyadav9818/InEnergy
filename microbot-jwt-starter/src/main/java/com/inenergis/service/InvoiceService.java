package com.inenergis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inenergis.dao.InvoiceDao;
import com.inenergis.entity.PortalUser;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.model.adapter.InvoiceAdapter;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by egamas on 04/10/2017.
 */
@Getter
@Setter
@Component
public class InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private PortalUserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Transactional("mysqlTransactionManager")
    public String getLastInvoiceByEmail(String email) throws JsonProcessingException {
        final PortalUser user = userService.getByEmail(email);
        final Invoice invoice = invoiceDao.getFirstByServiceAgreementServiceAgreementIdAndStatusOrderByDateToDesc(user.getServiceAgreement().getServiceAgreementId(), Invoice.InvoiceStatus.FINAL);
        invoice.getInvoiceLines().size(); //Lazy loading
        return objectMapper.writeValueAsString(InvoiceAdapter.build(invoice));
    }
}
