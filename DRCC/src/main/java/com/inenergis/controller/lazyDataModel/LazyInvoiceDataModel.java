package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.genericEnum.AgedInvoiceCategory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Map;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.ge;
import static org.hibernate.criterion.Restrictions.le;

public class LazyInvoiceDataModel extends LazyIdentifiableEntityDataModel<Invoice> {

    public LazyInvoiceDataModel(EntityManager entityManager, Map<String, Object> permanentFilters) {
        super(entityManager, Invoice.class, permanentFilters);
    }

    @Override
    protected void addFilter(Criteria criteria, String key, Map.Entry<String, Object> entry) {
        switch (key) {
            case "date":
                LocalDate date = (LocalDate) entry.getValue();
                criteria.add(ge("date", date.atStartOfDay()));
                criteria.add(le("date", date.atTime(23, 59, 59)));
                break;
            case "dueDateLessThan":
                criteria.add(and(Restrictions.lt("dueDate", entry.getValue())));
                break;
            case "paymentDateIsNull":
                criteria.add(and(Restrictions.isNull("paymentDate")));
                break;
            case "overdueDays":
                AgedInvoiceCategory agedInvoiceCategory = (AgedInvoiceCategory) entry.getValue();
                if (agedInvoiceCategory != AgedInvoiceCategory.VERY_OLD) {
                    criteria.add(and(Restrictions.ge("dueDate", LocalDate.now().minusDays(agedInvoiceCategory.getRange().getMaximum()))));
                }
                if (agedInvoiceCategory != AgedInvoiceCategory.CURRENT) {
                    criteria.add(and(Restrictions.le("dueDate", LocalDate.now().minusDays(agedInvoiceCategory.getRange().getMinimum()))));
                }
                break;
            case "paymentDate":
                if (entry.getValue() instanceof Boolean) {
                    if ((boolean) entry.getValue()) {
                        criteria.add(and(Restrictions.isNotNull("paymentDate")));
                    } else {
                        criteria.add(and(Restrictions.isNull("paymentDate")));
                    }
                } else {
                    super.addFilter(criteria, key, entry);
                }
                break;
            default:
                super.addFilter(criteria, key, entry);
                break;
        }
    }
}