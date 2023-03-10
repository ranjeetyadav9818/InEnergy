package com.inenergis.controller.admin;

import com.inenergis.controller.lazyDataModel.LazyBillingExceptionDataModel;
import com.inenergis.entity.billing.BillingException;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.service.InvoiceService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.HashMap;

import static com.inenergis.entity.billing.Invoice.InvoiceStatus.RETRYING;

@Getter
@Setter
@Named
@ViewScoped
public class BillingSolutionCenterController implements Serializable {

    private static final long serialVersionUID = 1L;

    Logger log = LoggerFactory.getLogger(BillingSolutionCenterController.class);

    @Inject
    UIMessage uiMessage;

    @Inject
    EntityManager entityManager;

    @Inject
    InvoiceService invoiceService;

    private LazyBillingExceptionDataModel exceptions;


    @PostConstruct
    public void init() {
        HashMap<String, Object> preFilters = new HashMap<>();
        preFilters.put("invoice.status", Invoice.InvoiceStatus.WITH_ERRORS);
        exceptions = new LazyBillingExceptionDataModel(entityManager, preFilters);
    }


    public void retry (BillingException exception) {
        exception.getInvoice().setStatus(RETRYING);
        invoiceService.saveOrUpdate(exception.getInvoice());
        uiMessage.addMessage("The invoice will be recalculated in a few minutes please come back to check that all the errors in the invoice have been solved");
    }
}