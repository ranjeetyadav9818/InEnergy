package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.InvoiceDao;
import com.inenergis.dao.InvoiceLineDao;
import com.inenergis.entity.PaymentDetailedStatistics;
import com.inenergis.entity.PaymentSeriesStatistics;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.InvoiceLine;
import com.inenergis.entity.program.rateProgram.RateProfileFee;
import com.inenergis.entity.program.rateProgram.RateTier;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Stateless
public class InvoiceService {
    @Inject
    private InvoiceDao invoiceDao;

    @Inject
    private InvoiceLineDao invoiceLineDao;

    public List<InvoiceLine> getBy(Long invoiceId, String servicePointId) {
        return invoiceLineDao.getBy(invoiceId, servicePointId);
    }

    public List<Invoice> getAll() {
        return invoiceDao.getAll();
    }

    public Invoice save(Invoice invoice) {
        return invoiceDao.save(invoice);
    }

    public List<Invoice> getByServiceAgreementYear(String serviceAgreementId, LocalDate year) {
        return invoiceDao.getByServiceAgreementYear(serviceAgreementId, year);
    }

    public Invoice getById(long invoiceId) {
        return invoiceDao.getById(invoiceId);
    }

    public Invoice saveOrUpdate(Invoice invoice) {
        return invoiceDao.saveOrUpdate(invoice);
    }

    public List<Invoice> getNewInvoices() {
        return invoiceDao.getNewInvoices();
    }

    public List<Invoice> getAllUnpaid() {
        return invoiceDao.getAllUnpaid();
    }

    public List<Invoice> getAllUnpaidDue() {
        return invoiceDao.getAllUnpaidDue();
    }

    public List<Invoice> getAllPaid() {
        return invoiceDao.getAllPaid();
    }

    public List<PaymentSeriesStatistics> getStat(int year, int month) {
        return invoiceDao.getStat(year,month);
    }

    public List<PaymentDetailedStatistics> getPaidUnpaidStat(int year, int month) {
        Map<LocalDate, PaymentDetailedStatistics> all;

        all = invoiceDao.getPaidStat(year,month).stream()
                .collect(Collectors.toMap(PaymentDetailedStatistics::getDate, Function.identity()));

        invoiceDao.getUnpaidStat(year,month).forEach(el -> {
            if (all.containsKey(el.getDate())) {
                all.get(el.getDate()).addUnpaid(el.getCount());
            } else {
                el.setPaidPercentage(0);
                all.put(el.getDate(), el);
            }
        });

        invoiceDao.getStat(year, month).forEach(stat ->
        {
            all.get(stat.getDate()).setTotal(stat.getTotal());
            all.get(stat.getDate()).setUnpaidAmount(stat.getUnpaid());
        });
        return new ArrayList<>(all.values());
    }

    public long countInvoicesWithFee(RateProfileFee fee){
        final List<CriteriaCondition> ancillaryConditions = Collections.singletonList(CriteriaCondition.builder().key("rateProfileAncillaryFee.id").value(fee.getId()).matchMode(MatchMode.EXACT).build());
        final List<CriteriaCondition> rateConditions = Collections.singletonList(CriteriaCondition.builder().key("rateProfileConsumptionFee.id").value(fee.getId()).matchMode(MatchMode.EXACT).build());
        return invoiceLineDao.countWithCriteria(ancillaryConditions) + invoiceLineDao.countWithCriteria(rateConditions);
    }

    public boolean existInvoicesLinesForTier(RateTier tier){
        final List<CriteriaCondition> ancillaryConditions = Collections.singletonList(CriteriaCondition.builder().key("tier.id").value(tier.getId()).matchMode(MatchMode.EXACT).build());
        return invoiceLineDao.getWithCriteria(ancillaryConditions).stream().findFirst().isPresent() ;
    }
}