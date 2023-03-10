package com.inenergis.model;

import com.inenergis.entity.billing.Invoice;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 05/09/2017.
 */
public class ElasticInvoiceConverter {

    public static ElasticInvoice convert(Invoice invoice) {
        final ElasticInvoice.ElasticInvoiceBuilder builder = ElasticInvoice.builder();
        if (StringUtils.isNotEmpty(invoice.getInvoiceNumber())) {
            builder.invoiceNumber(invoice.getInvoiceNumber());
        }
        if (invoice.getDueDate() != null) {
            builder.dueDate(invoice.getDueDate());
        }
        if (StringUtils.isNotEmpty(invoice.getDescription())) {
            builder.description(invoice.getDescription());
        }
        if (invoice.getDateFrom() != null) {
            builder.dateFrom(invoice.getDateFrom());
        }
        if (invoice.getDateTo() != null) {
            builder.dateTo(invoice.getDateTo());
        }
        if (invoice.getPaymentDate() != null) {
            builder.paymentDate(invoice.getPaymentDate());
        }

        if (invoice.getServiceAgreement() != null) {
            builder.serviceAgreement(invoice.getServiceAgreement().getServiceAgreementId());
            if (CollectionUtils.isNotEmpty(invoice.getServiceAgreement().getAgreementPointMaps())) {
                List<String> servicePoints = invoice.getServiceAgreement().getAgreementPointMaps()
                        .stream()
                        .filter(apm -> StringUtils.isNotEmpty(apm.getServicePoint().getServicePointId()))
                        .map(apm -> apm.getServicePoint().getServicePointId()).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(servicePoints)) {
                    builder.servicePoints(servicePoints);
                }
            }
        }

        builder.total(invoice.getTotal());

        return builder.build();
    }
}
