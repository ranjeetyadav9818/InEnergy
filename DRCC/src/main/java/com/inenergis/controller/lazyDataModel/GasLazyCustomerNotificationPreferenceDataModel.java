package com.inenergis.controller.lazyDataModel;


import com.inenergis.controller.customerData.GasAgreementPointMapList;
import com.inenergis.entity.GasCustomerNotificationPreference;
import com.inenergis.entity.GasServiceAgreement;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
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

@Transactional
public class GasLazyCustomerNotificationPreferenceDataModel extends LazyDataModel<GasCustomerNotificationPreference> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	Logger log =  LoggerFactory.getLogger(GasLazyCustomerNotificationPreferenceDataModel.class);

	// passed in entities to calculate on
//	private ServiceAgreement serviceAgreement;
//	private Person person;
//	private Account account;

	// passed in strings to calculate on
	private String personId;
	private String serviceAgreementId;
	private String name;
	private String address;
	private String premisesId;
	private String accountId;
	private String phone;
	private String postCode;
	private String city;

	private List<GasCustomerNotificationPreference> datasource = new ArrayList<GasCustomerNotificationPreference>();
	private GasAgreementPointMapList agreementPointMapList;

//	private boolean accountJoined = false;
//	private boolean personJoined = false;

	private GasServiceAgreement emptyServiceAgreement = new GasServiceAgreement();

	public GasLazyCustomerNotificationPreferenceDataModel(GasAgreementPointMapList agreementPointMapList){
		this.agreementPointMapList = agreementPointMapList;
	}

	public GasLazyCustomerNotificationPreferenceDataModel(String personId, String saId, String name, String address, String city, String postCode, String premisesId, String accountId, String phone, GasAgreementPointMapList agreementPointMapList){
		this.personId = personId;
		this.serviceAgreementId = saId;
		this.name = name;
		this.address = address;
		this.postCode = postCode;
		this.city = city;
		this.premisesId = premisesId;
		this.accountId = accountId;
		this.phone = emptyServiceAgreement.stripOutNoDigit(phone);
		this.agreementPointMapList = agreementPointMapList;
	}

	@Override
	public Object getRowKey(GasCustomerNotificationPreference notificationPreference){
		return notificationPreference.getRowKey();
	}

	@Override
	public GasCustomerNotificationPreference getRowData(String rowKey){
		Long l = Long.valueOf(rowKey);
		for(GasCustomerNotificationPreference notificationPreference : datasource){
			if(notificationPreference.getCustomerNotificationPreferenceId().equals(l)){
				return notificationPreference;
			}
		}
		return null;
	}

	@Override
	public List<GasCustomerNotificationPreference> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {

		Session session = (Session)agreementPointMapList.getEntityManager().getDelegate();
		Criteria c = session.createCriteria(GasCustomerNotificationPreference.class);
		populateFilterCriteria(filters, c);
		c.setFirstResult(first);
		c.setMaxResults(pageSize);

		datasource = c.list();

		//rowCount

		Criteria c2 = session.createCriteria(GasCustomerNotificationPreference.class);
		populateFilterCriteria(filters,c2);
		c2.setProjection(Projections.rowCount());

		Long count = (Long) c2.uniqueResult();

		this.setRowCount(count.intValue());

        return datasource;
	}

	private void populateFilterCriteria(Map<String, Object> filters, Criteria c) {
		c.createAlias("serviceAgreement", "sa");
		c.createAlias("sa.account", "a");
		c.createAlias("a.person", "pers");
		c.createAlias("sa.agreementPointMaps", "apm");
		c.createAlias("apm.servicePoint", "sp");
		c.createAlias("sp.premise", "prem");


		if(StringUtils.isNotBlank(this.serviceAgreementId)){
            c.add(like("sa.serviceAgreementId",this.serviceAgreementId, MatchMode.START));
        }
		if(StringUtils.isNotBlank(this.personId)){
            c.add(like("pers.personId",this.personId, MatchMode.START));
        }
		if(StringUtils.isNotBlank(this.name)){
            c.add(Restrictions.disjunction()
                    .add(like("pers.customerName", this.name, MatchMode.START))
                    .add(like("pers.businessName", this.name, MatchMode.START))
                    );
        }
		if(StringUtils.isNotBlank(this.address)){
            c.add(like("prem.serviceAddress1", this.address, MatchMode.START));
        }
		if(StringUtils.isNotBlank(this.premisesId)){
            c.add(like("prem.premiseId", this.premisesId, MatchMode.START));
        }
		if(StringUtils.isNotBlank(this.accountId)){
            c.add(like("a.accountId", this.accountId, MatchMode.START));
        }
		if(StringUtils.isNotBlank(this.phone)){
            c.add(like("sa.phone", this.phone, MatchMode.START));
        }
		if(StringUtils.isNotBlank(this.postCode)){
            c.add(like("prem.servicePostal", this.postCode, MatchMode.START));
        }
		if(StringUtils.isNotBlank(this.city)){
            c.add(like("prem.serviceCityUpr", this.city, MatchMode.START));
        }

		if(filters!=null && !filters.isEmpty()){
            if(filters.containsKey("saId")){
                c.add(like("sa.serviceAgreementId", ((String)filters.get("saId")).trim(), MatchMode.START));
            }

            if(filters.containsKey("acctId")){
                c.add(like("a.accountId", ((String)filters.get("acctId")).trim(), MatchMode.START));
            }

            if(filters.containsKey("spId")){
                c.add(like("sp.servicePointId", ((String)filters.get("spId")).trim(), MatchMode.START));
            }

            if(filters.containsKey("premId")){
                c.add(like("prem.premiseId", ((String)filters.get("premId")).trim(), MatchMode.START));
            }

            if(filters.containsKey("druid")){
                c.add(like("sa.druid", ((String)filters.get("druid")).trim(), MatchMode.START));
            }

            if(filters.containsKey("address")){
                c.add(like("prem.serviceAddress1", ((String)filters.get("address")).trim(), MatchMode.START));
            }

            if(filters.containsKey("personId")){
                c.add(like("pers.personId", ((String)filters.get("personId")).trim(), MatchMode.START));
            }

            if(filters.containsKey("businessName")){
                c.add(like("pers.businessName", ((String)filters.get("businessName")).trim(), MatchMode.START));
            }

            if(filters.containsKey("program")){
                c.add(like("notificationProgram", ((String)filters.get("program")).trim(), MatchMode.START));
            }

            if(filters.containsKey("language")){
                c.add(like("notificationLanguage", ((String)filters.get("language")).trim(), MatchMode.START));
            }

            if(filters.containsKey("channel")){
                c.add(like("notificationTypeId", ((String)filters.get("channel")).trim(), MatchMode.START));
            }

			if(filters.containsKey("notificationValue")){
				c.add(like("notificationValue", ((String)filters.get("notificationValue")), MatchMode.START));
			}

            if(filters.containsKey("stat")){
                if(filters.containsKey("stat")){
                    c.add(like("sa.saStatus", ((String)filters.get("stat")), MatchMode.EXACT));
                }
            }
        }
	}

	public List<GasCustomerNotificationPreference> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<GasCustomerNotificationPreference> datasource) {
		this.datasource = datasource;
	}


}
