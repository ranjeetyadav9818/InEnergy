package com.inenergis.controller.fileProcessorLog.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Named;

import com.inenergis.controller.events.EventList;
import com.inenergis.entity.GasPdpSrEvent;
import com.inenergis.entity.GasPdpSrNotification;
import com.inenergis.entity.GasPdpSrParticipant;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.inenergis.entity.GasPdpSrParticipant.SuccessfulNotificationType;

import static org.hibernate.criterion.Restrictions.*;

@Named
public class GasLazyNotificationDataModel extends LazyDataModel<GasPdpSrNotification> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	Logger log =  LoggerFactory.getLogger(GasLazyNotificationDataModel.class);
	
	private GasPdpSrEvent event;
	private List<GasPdpSrNotification> datasource = new ArrayList<GasPdpSrNotification>();
	private NotificationType type = NotificationType.ALL; 
	private EventList eventList;
	private String filterString = null;
	private String enpValue = null;
	private GasPdpSrParticipant participant;
	private boolean participantJoined = false;
	
	public enum NotificationType {
		ALL, VENDOR, UNSUCCESSFUL, ENP, PARTICIPANT_REQUESTED
	}
	
	public GasLazyNotificationDataModel(EventList eventList, GasPdpSrEvent event){
		this.event = event;
		this.eventList = eventList;
	}
	
	public GasLazyNotificationDataModel(EventList eventList, GasPdpSrEvent event, NotificationType type){
		this.event = event;
		this.type = type;
		this.eventList = eventList;
	}
	
	public GasLazyNotificationDataModel(EventList eventList, GasPdpSrEvent event, NotificationType type, String enpValue){
		this.event = event;
		this.type = type;
		this.eventList = eventList;
		this.enpValue = enpValue;
	}
	
	public GasLazyNotificationDataModel(EventList eventList, GasPdpSrEvent event, NotificationType type, GasPdpSrParticipant participant){
		this.event = event;
		this.type = type;
		this.eventList = eventList;
		this.participant = participant;
	}
	
	@Override
	public GasPdpSrNotification getRowData(String rowKey){
		Long l = Long.valueOf(rowKey);
		for(GasPdpSrNotification notification : datasource){
			if(notification.getNotificationId().equals(l)){
				return notification;
			}
		}
		return null;
	}
	
	@Override
	public Object getRowKey(GasPdpSrNotification notification){
		return notification.getNotificationId();
	}
	
	@Override
	public List<GasPdpSrNotification> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
		this.participantJoined = false;
		
		if(eventList != null){
			if(this.event!=null){
				Session session = (Session)eventList.getEntityManager().getDelegate();
				Criteria searchCriteria = session.createCriteria(GasPdpSrNotification.class);

				assignFiltersToCriteria(searchCriteria,filters);

				searchCriteria.setFirstResult(first);
				searchCriteria.setMaxResults(pageSize);

				assignSortAndOrderToCriteria(sortField, sortOrder, searchCriteria);

				datasource = searchCriteria.list();
				
				this.participantJoined = false;
		 
		        //rowCount
				StringBuilder stringBuilderForFilter = new StringBuilder();
				for(Entry<String, Object> e : filters.entrySet()){
					stringBuilderForFilter.append(e.getKey());
					stringBuilderForFilter.append(e.getValue());
				}
				if(filterString == null || !filterString.equals(stringBuilderForFilter.toString())){
					Criteria countSearchCriteria = session.createCriteria(GasPdpSrNotification.class);

					assignFiltersToCriteria(countSearchCriteria,filters);
					
					countSearchCriteria.setProjection(Projections.rowCount());
					
					Long count = (Long) countSearchCriteria.uniqueResult();
					
			        this.setRowCount(count.intValue());
					
			        filterString = stringBuilderForFilter.toString();
			        
				}
		        
			} else {
				log.info("******************** Event is null: {}", this.event);
			}
			
		} else {
			log.info("******************** entityList is null: {}", eventList);
		}
        
        return datasource;
	}

	private void assignFiltersToCriteria(Criteria searchCriteria, Map<String, Object> filters) {
		searchCriteria.add(eq("pdpSrEvent.eventId",this.event.getEventId()));
		if(NotificationType.VENDOR.equals(type)){
			searchCriteria.add(ne("vendorStatus", "DUPLICATE"));
		} else if(NotificationType.UNSUCCESSFUL.equals(type)){
			searchCriteria.add(ne("vendorStatus", "DUPLICATE"));
			this.createParticipantAlias(searchCriteria);
			searchCriteria.add(eq("p.successfulNotification", SuccessfulNotificationType.UNSUCCESS));
		} else if(NotificationType.ENP.equals(type)){
			searchCriteria.add(eq("notifyByValue", enpValue));
		} else if(NotificationType.PARTICIPANT_REQUESTED.equals(type)){
			searchCriteria.add(eq("pdpSrParticipant", this.participant));
			searchCriteria.add(Restrictions.isNotNull("pdpSrVendor")); // exclude manual notifications
		}

		if(filters!=null && !filters.isEmpty()){

			if(filters.containsKey("saId")){
				this.createParticipantAlias(searchCriteria);
				searchCriteria.add(like("p.saId", ((String)filters.get("saId")).trim(), MatchMode.START));
			}

			if(filters.containsKey("servicePointId")){
				this.createParticipantAlias(searchCriteria);
				searchCriteria.add(like("p.servicePointId", ((String)filters.get("servicePointId")).trim(), MatchMode.START));
			}

			if(filters.containsKey("premiseId")){
				this.createParticipantAlias(searchCriteria);
				searchCriteria.add(like("p.premiseId", ((String)filters.get("premiseId")).trim(), MatchMode.START));
			}

			if(filters.containsKey("acctId")){
				this.createParticipantAlias(searchCriteria);
				searchCriteria.add(like("p.acctId", ((String)filters.get("acctId")).trim(), MatchMode.START));
			}

			if(filters.containsKey("druid")){
				this.createParticipantAlias(searchCriteria);
				searchCriteria.add(like("p.druid", ((String)filters.get("druid")).trim(), MatchMode.START));
			}

			if(filters.containsKey("serviceAddress")){
				this.createParticipantAlias(searchCriteria);
				searchCriteria.add(like("p.serviceAddress", ((String)filters.get("serviceAddress")).trim(), MatchMode.ANYWHERE));
			}

			if(filters.containsKey("vendor")){
				final String vendor = ((String) filters.get("vendor")).trim();
				if("Manual".equalsIgnoreCase(vendor)){
					searchCriteria.add(isNull("pdpSrVendor"));
				}else{
					searchCriteria.createAlias("pdpSrVendor", "v").add(like("v.vendor", vendor, MatchMode.START));
				}
			}

			if(filters.containsKey("notifyBy")){
				searchCriteria.add(like("notifyBy", ((String)filters.get("notifyBy")).trim(), MatchMode.START));
			}

			if(filters.containsKey("language")){
				searchCriteria.add(like("language", ((String)filters.get("language")).trim(), MatchMode.START));
			}

			if(filters.containsKey("successfulNotification")){
				if("Success".equals((String)filters.get("successfulNotification"))){
					searchCriteria.add(eq("successfulNotification", (SuccessfulNotificationType.DELIVERED)));
				} else if("Fail".equals((String)filters.get("successfulNotification"))){
					searchCriteria.add(disjunction()
							.add(eq("successfulNotification", (SuccessfulNotificationType.ATTEMPTED)))
							.add(eq("successfulNotification", (SuccessfulNotificationType.UNSUCCESS)))
					);
				}
			}

			if(filters.containsKey("notifyByValue")){
				searchCriteria.add(like("notifyByValue", ((String)filters.get("notifyByValue")).trim(), MatchMode.START));
			}

			if(filters.containsKey("vendorStatusDisplayMessage")){
				searchCriteria.add(like("vendorStatusDisplayMessage", ((String)filters.get("vendorStatusDisplayMessage")).trim(), MatchMode.START));
			}
		}
	}

	private void assignSortAndOrderToCriteria(String sortField, SortOrder sortOrder, Criteria searchCriteria) {
		if(sortField!=null){
			if("saId".equals(sortField) && this.participantJoined){
				sortField = "p.saId";
			} else if("servicePointId".equals(sortField) && this.participantJoined){
				sortField = "p.servicePointId";
			} else if("premiseId".equals(sortField) && this.participantJoined){
				sortField = "p.premiseId";
			} else if("acctId".equals(sortField) && this.participantJoined){
				sortField = "p.acctId";
			} else if("druid".equals(sortField) && this.participantJoined){
				sortField = "p.druid";
			} else if("serviceAddress".equals(sortField) && this.participantJoined){
				sortField = "p.serviceAddress";
			} else if("vendor".equals(sortField)){
				sortField = "v.vendor";
			} else if("channel".equals(sortField)){
				sortField = "notifyBy";
			}
			if(SortOrder.ASCENDING.equals(sortOrder)){
				searchCriteria.addOrder(Order.asc(sortField));
			} else if(SortOrder.DESCENDING.equals(sortOrder)){
				searchCriteria.addOrder(Order.desc(sortField));
			}
		} else {
			searchCriteria.addOrder(Order.asc("notificationId"));
		}
	}

	public void createParticipantAlias(Criteria c){
		if(!this.participantJoined){
//			new Exception().printStackTrace();
			c.createAlias("pdpSrParticipant", "p");
			this.participantJoined = true;
		}
	}

	public List<GasPdpSrNotification> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<GasPdpSrNotification> datasource) {
		this.datasource = datasource;
	}
	
}
