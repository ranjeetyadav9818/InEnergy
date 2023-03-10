package com.inenergis.dao;

import com.inenergis.entity.billing.InvoiceLine;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Stateless
@Transactional
public class InvoiceLineDao extends GenericDao<InvoiceLine> {
    public InvoiceLineDao() {
        setClazz(InvoiceLine.class);
    }

    public List<InvoiceLine> getBy(Long invoiceId, String servicePointId) {
        final List<CriteriaCondition> criteriaConditions = Arrays.asList(
                CriteriaCondition.builder().key("servicePoint.servicePointId").matchMode(MatchMode.EXACT).value(servicePointId).build(),
                CriteriaCondition.builder().key("invoice.id").matchMode(MatchMode.EXACT).value(invoiceId).build()
        );
        return getWithCriteria(criteriaConditions);
    }
}
