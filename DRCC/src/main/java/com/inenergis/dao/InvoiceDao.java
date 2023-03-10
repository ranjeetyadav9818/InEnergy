package com.inenergis.dao;

import com.inenergis.entity.PaymentDetailedStatistics;
import com.inenergis.entity.PaymentSeriesStatistics;
import com.inenergis.entity.billing.Invoice;
import org.hibernate.Criteria;
import org.hibernate.Session;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ge;
import static org.hibernate.criterion.Restrictions.isNotEmpty;
import static org.hibernate.criterion.Restrictions.isNotNull;
import static org.hibernate.criterion.Restrictions.isNull;
import static org.hibernate.criterion.Restrictions.le;
import static org.hibernate.criterion.Restrictions.lt;

@Stateless
@Transactional
public class InvoiceDao extends GenericDao<Invoice> {

    public InvoiceDao() {
        setClazz(Invoice.class);
    }

    @SuppressWarnings("unchecked")
    public List<Invoice> getByServiceAgreementYear(String serviceAgreementId, LocalDate year) {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());
        criteria.add(eq("serviceAgreement.id", serviceAgreementId));
        criteria.add(le("dateFrom", year));
        criteria.add(ge("dateFrom", year.minusYears(1).minusDays(1)));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Invoice> getNewInvoices() {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());
        criteria.add(isNull("dueDate"));
        criteria.add(isNull("invoiceNumber"));
        criteria.add(isNotEmpty("invoiceLines"));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Invoice> getAllUnpaid() {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());
        criteria.add(isNull("paymentDate"));
        criteria.add(isNotNull("dueDate"));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Invoice> getAllUnpaidDue() {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());
        criteria.add(isNull("paymentDate"));
        criteria.add(lt("dueDate", LocalDate.now()));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Invoice> getAllPaid() {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteria = session.createCriteria(getClazz());
        criteria.add(isNotNull("paymentDate"));
        criteria.add(isNotNull("dueDate"));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<PaymentSeriesStatistics> getStat(int year, int month) {
        return entityManager.createQuery(
                "SELECT NEW com.inenergis.entity.PaymentSeriesStatistics(s.serial, s.date, sum(l.total), sum( case when i.paymentDate is null then l.total else 0L end))" +
                        " FROM BillingCycleSchedule s, Invoice i, InvoiceLine l " +
                        "WHERE s.date=DATE(i.date) and year(s.date) = :year and month(s.date) = :month AND i.id = l.invoice.id "+
                        "GROUP BY s.serial, s.date ORDER BY s.date DESC")
                .setMaxResults(21)
                .setParameter("year",year)
                .setParameter("month",month)
                .getResultList();

    }

    @SuppressWarnings("unchecked")
    public List<PaymentDetailedStatistics> getPaidStat(int year , int month) {
        year =2021;
        month =10;
        return entityManager.createQuery(
                "SELECT NEW com.inenergis.entity.PaymentDetailedStatistics(s.serial, s.date, count(*), true) " +
                        "FROM BillingCycleSchedule s, Invoice i " +
                        "WHERE s.date=DATE(i.date) and year(s.date) = :year and month(s.date) = :month AND i.paymentDate is not null " +
                        "GROUP BY s.serial, s.date ORDER BY s.date DESC")
                .setMaxResults(21)
                .setParameter("year",year)
                .setParameter("month",month)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PaymentDetailedStatistics> getUnpaidStat(int year, int month) {
        return entityManager.createQuery(
                "SELECT NEW com.inenergis.entity.PaymentDetailedStatistics(s.serial, s.date, count(*), false) " +
                        "FROM BillingCycleSchedule s, Invoice i " +
                        "WHERE s.date=DATE(i.date) and year(s.date) = :year and month(s.date) = :month AND i.paymentDate is null " +
                        "GROUP BY s.serial, s.date ORDER BY s.date DESC")
                .setMaxResults(21)
                .setParameter("year",year)
                .setParameter("month",month)
                .getResultList();
    }
}