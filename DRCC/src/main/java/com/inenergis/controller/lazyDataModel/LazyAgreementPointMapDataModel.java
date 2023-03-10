package com.inenergis.controller.lazyDataModel;

import com.inenergis.controller.customerData.AgreementPointMapList;
import com.inenergis.controller.servicePointData.ServicePointList;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ServiceAgreement;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.or;

@Transactional
public class LazyAgreementPointMapDataModel extends LazyDataModel<AgreementPointMap> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String PHONE = "phone";

    Logger log = LoggerFactory.getLogger(LazyAgreementPointMapDataModel.class);

    // passed in strings to search on
    private String personId;
    private String serviceAgreementId;
    private String name;
    private String address;
    private String premisesId;
    private String accountId;
    private String phone;
    private String postCode;
    private String city;
    private String servicePointId;

    private List<AgreementPointMap> datasource = new ArrayList<>();
    private AgreementPointMapList agreementPointMapList;
    private ServicePointList servicePointList;

    public LazyAgreementPointMapDataModel(AgreementPointMapList agreementPointMapList) {
        this.agreementPointMapList = agreementPointMapList;
    }

    public LazyAgreementPointMapDataModel(String personId, String saId, String name, String address, String city, String postCode, String premisesId, String accountId, String phone, AgreementPointMapList agreementPointMapList) {
        this.personId = personId;
        this.serviceAgreementId = saId;
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.city = city;
        this.premisesId = premisesId;
        this.accountId = accountId;
        this.phone = phone;
        this.agreementPointMapList = agreementPointMapList;
    }

    public LazyAgreementPointMapDataModel(String address, String city, String postCode, String servicePointId, String premisesId, ServicePointList servicePointList) {
        this.personId = null;
        this.serviceAgreementId = null;
        this.name = null;
        this.address = address;
        this.postCode = postCode;
        this.city = city;
        this.servicePointId = servicePointId;
        this.premisesId = premisesId;
        this.accountId = null;
        this.phone = null;
        this.servicePointList = servicePointList;
    }

    @Override
    public AgreementPointMap getRowData(String rowKey) {
        for (AgreementPointMap amp : datasource) {
            if (amp.getRowKey().equals(rowKey)) {
                return amp;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(AgreementPointMap amp) {
        return amp.getRowKey();
    }

    @Override
    public List<AgreementPointMap> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        if (filters != null && filters.containsKey(PHONE)) {
            filters.replace(PHONE, ServiceAgreement.stripOutNoDigit((String) filters.get(PHONE)));
        }

        Session session;
        if (servicePointList != null) {
            session = (Session) servicePointList.getEntityManager().getDelegate();
        } else {
            session = (Session) agreementPointMapList.getEntityManager().getDelegate();
        }
        Criteria c = session.createCriteria(AgreementPointMap.class);

        populateFilterCriteria(filters, c);
        c.setFirstResult(first);
        c.setMaxResults(pageSize);

        datasource = c.list();

        setRowCount(filters, session, datasource, first, pageSize);

        return datasource;
    }

    private void setRowCount(Map<String, Object> filters, Session session, List<AgreementPointMap> datasource, int first, int pageSize) {
        int rowCount = 0;
        if (areThereSearchesById()) {
            Criteria c2 = session.createCriteria(AgreementPointMap.class);

            populateFilterCriteria(filters, c2);

            c2.setProjection(Projections.rowCount());

            Long count = (Long) c2.uniqueResult();
            rowCount = count.intValue();
        } else {
            if (datasource.size() < pageSize) {
                rowCount = first + datasource.size();
            } else {
                rowCount = first + (pageSize * 10) + 1;
            }
        }
        this.setRowCount(rowCount);
    }

    private boolean areThereSearchesById() {
        return this.accountId != null || this.personId != null || this.premisesId != null || this.serviceAgreementId != null || this.servicePointId != null;
    }

    private void populateFilterCriteria(Map<String, Object> filters, Criteria c) {
        c.createAlias("serviceAgreement", "sa");
        c.createAlias("sa.account", "a");
        c.createAlias("a.person", "pers");

        c.createAlias("servicePoint", "sp");
        c.createAlias("sp.premise", "prem");
        //c.createAlias("sp.meter", "mtr");


        if (StringUtils.isNotBlank(this.serviceAgreementId)) {
            c.add(like("sa.serviceAgreementId", this.serviceAgreementId, MatchMode.START));
        }
        if (StringUtils.isNotBlank(this.personId)) {
            c.add(like("pers.personId", this.personId, MatchMode.START));
        }
        if (StringUtils.isNotBlank(this.name)) {
            Conjunction customerNameConjunction = Restrictions.and();
            Conjunction businessNameConjunction = Restrictions.and();
            for (String namePart : this.name.split(" ")) {
                customerNameConjunction.add(like("pers.customerName", namePart, MatchMode.START));
                businessNameConjunction.add(like("pers.businessName", namePart, MatchMode.START));
            }

            c.add(Restrictions.disjunction().add(customerNameConjunction).add(businessNameConjunction));
        }
        if (StringUtils.isNotBlank(this.address)) {
            c.add(like("prem.serviceAddress1", this.address, MatchMode.START));
        }
        if (StringUtils.isNotBlank(this.premisesId)) {
            c.add(like("prem.premiseId", this.premisesId, MatchMode.START));
        }
        if (StringUtils.isNotBlank(this.accountId)) {
            c.add(like("a.accountId", this.accountId, MatchMode.START));
        }
        if (StringUtils.isNotBlank(ServiceAgreement.stripOutNoDigit(this.phone))) {
            c.add(like("sa.phone", ServiceAgreement.stripOutNoDigit(this.phone), MatchMode.START));
        }
        if (StringUtils.isNotBlank(this.postCode)) {
            c.add(like("prem.servicePostal", this.postCode, MatchMode.START));
        }
        if (StringUtils.isNotBlank(this.city)) {
            c.add(like("prem.serviceCityUpr", this.city, MatchMode.START));
        }
        if (StringUtils.isNotBlank(this.servicePointId)) {
            c.add(like("sp.servicePointId", this.servicePointId, MatchMode.START));
        }


        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("saId")) {
                c.add(like("sa.serviceAgreementId", ((String) filters.get("saId")).trim(), MatchMode.START));
            }

            if (filters.containsKey("acctId")) {
                c.add(like("a.accountId", ((String) filters.get("acctId")).trim(), MatchMode.START));
            }

            if (filters.containsKey("spId")) {
                c.add(like("sp.servicePointId", ((String) filters.get("spId")).trim(), MatchMode.START));
            }

            if (filters.containsKey("premId")) {
                c.add(like("prem.premiseId", ((String) filters.get("premId")).trim(), MatchMode.START));
            }

            if (filters.containsKey("druid")) {
                c.add(like("sa.druid", ((String) filters.get("druid")).trim(), MatchMode.START));
            }

            if (filters.containsKey("address")) {
                c.add(like("prem.serviceAddress1", ((String) filters.get("address")).trim(), MatchMode.START));
            }

            if (filters.containsKey("personId")) {
                c.add(like("pers.personId", ((String) filters.get("personId")).trim(), MatchMode.START));
            }

            if (filters.containsKey("busName")) {
                c.add(
                        or(
                                like("pers.businessName", ((String) filters.get("busName")).trim(), MatchMode.START),
                                like("pers.customerName", ((String) filters.get("busName")).trim(), MatchMode.START)));
            }

            if (filters.containsKey("custName")) {
                c.add(
                        or(
                                like("pers.businessName", ((String) filters.get("custName")).trim(), MatchMode.START),
                                like("pers.customerName", ((String) filters.get("custName")).trim(), MatchMode.START)));
            }

            if (filters.containsKey("phone")) {
                c.add(like("sa.phone", ((String) filters.get("phone")).trim(), MatchMode.START));
            }

            if (filters.containsKey("serviceCityUpr")) {
                c.add(like("prem.serviceCityUpr", ((String) filters.get("serviceCityUpr")).trim(), MatchMode.START));
            }

            if (filters.containsKey("postCode")) {
                c.add(like("prem.servicePostal", ((String) filters.get("postCode")).trim(), MatchMode.START));
            }

            if (filters.containsKey("stat")) {
                c.add(like("sa.saStatus", ((ServiceAgreement.SaStatus) filters.get("stat")).getValue(), MatchMode.EXACT));
            }

            if (filters.containsKey("meterId")) {
                c.add(like("mtr.meterId", ((String) filters.get("meterId")).trim(), MatchMode.START));
            }

            if (filters.containsKey("meterBadgeNumber")) {
                c.add(like("mtr.badgeNumber", ((String) filters.get("meterBadgeNumber")).trim(), MatchMode.START));
            }
        }
    }

    public List<AgreementPointMap> getDatasource() {
        return datasource;
    }

    public void setDatasource(List<AgreementPointMap> datasource) {
        this.datasource = datasource;
    }

    public String getServicePointId() {
        return servicePointId;
    }

    public void setServicePointId(String servicePointId) {
        this.servicePointId = servicePointId;
    }
}