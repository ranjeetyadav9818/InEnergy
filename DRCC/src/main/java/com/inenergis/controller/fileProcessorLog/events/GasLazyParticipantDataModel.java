package com.inenergis.controller.fileProcessorLog.events;

import static org.hibernate.criterion.Restrictions.conjunction;
import static org.hibernate.criterion.Restrictions.disjunction;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.like;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import com.inenergis.entity.GasPdpSrEvent;
import com.inenergis.entity.GasPdpSrParticipant;
import  com.inenergis.controller.events.EventList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.inenergis.entity.GasPdpSrParticipant.SuccessfulNotificationType;

@Transactional
public class GasLazyParticipantDataModel extends LazyDataModel<GasPdpSrParticipant> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	Logger log =  LoggerFactory.getLogger(GasLazyParticipantDataModel.class);
	
	private GasPdpSrEvent event;
	private List<GasPdpSrParticipant> datasource = new ArrayList<GasPdpSrParticipant>();
	private EventList eventList;
	private String filterString = null;
	private SuccessfulNotificationType participantSuccessType;
	
	public GasLazyParticipantDataModel(EventList eventList, GasPdpSrEvent event){
		this.event = event;
		this.eventList = eventList;
	}
	
	public GasLazyParticipantDataModel(EventList eventList, GasPdpSrEvent event, SuccessfulNotificationType type){
		this.event = event;
		this.eventList = eventList;
		this.participantSuccessType = type;
	}
	
	@Override
	public GasPdpSrParticipant getRowData(String rowKey){
		Long l = Long.valueOf(rowKey);
		for(GasPdpSrParticipant participant : datasource){
			if(participant.getParticipantId().equals(l)){
				return participant;
			}
		}
		return null;
	}
	
	@Override
	public Object getRowKey(GasPdpSrParticipant participant){
		return participant.getParticipantId();
	}
	
	@Override
	public List<GasPdpSrParticipant> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
		
		if(eventList != null){
			if(this.event!=null){
				Session session = (Session)eventList.getEntityManager().getDelegate();
				Criteria c = session.createCriteria(GasPdpSrParticipant.class);
				c.add(eq("pdpSrEvent",this.event));
				if(SuccessfulNotificationType.DELIVERED.equals(this.participantSuccessType)){
					c.add(eq("successfulNotification", SuccessfulNotificationType.DELIVERED));
				} else if(SuccessfulNotificationType.UNSUCCESS.equals(this.participantSuccessType)){
					c.add(eq("successfulNotification", SuccessfulNotificationType.UNSUCCESS));
				} else if(SuccessfulNotificationType.ATTEMPTED.equals(this.participantSuccessType)){
					c.add(eq("successfulNotification", SuccessfulNotificationType.ATTEMPTED));
				} 
				
				if(filters!=null && !filters.isEmpty()){
					if(filters.containsKey("saId")){
						c.add(like("saId", ((String)filters.get("saId")).trim(), MatchMode.START));
					}
					
					if(filters.containsKey("servicePointId")){
						c.add(like("servicePointId", ((String)filters.get("servicePointId")).trim(), MatchMode.START));
					}
					
					if(filters.containsKey("premiseId")){
						c.add(like("premiseId", ((String)filters.get("premiseId")).trim(), MatchMode.START));
					}
					
					if(filters.containsKey("acctId")){
						c.add(like("acctId", ((String)filters.get("acctId")).trim(), MatchMode.START));
					}
					
					if(filters.containsKey("druid")){
						c.add(like("druid", ((String)filters.get("druid")).trim(), MatchMode.START));
					}
					
					if(filters.containsKey("serviceAddress")){
						c.add(like("serviceAddress", ((String)filters.get("serviceAddress")).trim(), MatchMode.ANYWHERE));
					}
					
					if(filters.containsKey("successfulNotification")){
						if("Success".equals((String)filters.get("successfulNotification"))){
							c.add(eq("successfulNotification", (SuccessfulNotificationType.DELIVERED)));
						} else if("Fail".equals((String)filters.get("successfulNotification"))){
							c.add(disjunction()
									.add(eq("successfulNotification", (SuccessfulNotificationType.ATTEMPTED)))
									.add(eq("successfulNotification", (SuccessfulNotificationType.UNSUCCESS)))
							);
						}
					}

				}
				c.setFirstResult(first);
				c.setMaxResults(pageSize);
				if(sortField!=null){
					if(SortOrder.ASCENDING.equals(sortOrder)){
						c.addOrder(Order.asc(sortField));
					} else if(SortOrder.DESCENDING.equals(sortOrder)){
						c.addOrder(Order.desc(sortField));
					}
				} else {
					c.addOrder(Order.asc("participantId"));
				}
				
				datasource = c.list();
		 
		        //rowCount
				StringBuilder s = new StringBuilder();
				for(Entry<String, Object> e : filters.entrySet()){
					s.append(e.getKey());
					s.append(e.getValue());
				}
				
				if(filterString == null || !filterString.equals(s.toString())){
					Criteria c2 = session.createCriteria(GasPdpSrParticipant.class);
					c2.add(eq("pdpSrEvent",this.event));
					if(SuccessfulNotificationType.DELIVERED.equals(this.participantSuccessType)){
						c2.add(eq("successfulNotification", SuccessfulNotificationType.DELIVERED));
					} else if(SuccessfulNotificationType.UNSUCCESS.equals(this.participantSuccessType)){
						c2.add(eq("successfulNotification", SuccessfulNotificationType.UNSUCCESS));
					} else if(SuccessfulNotificationType.ATTEMPTED.equals(this.participantSuccessType)){
						c2.add(eq("successfulNotification", SuccessfulNotificationType.ATTEMPTED));
					} 
					
					if(filters!=null && !filters.isEmpty()){
						if(filters.containsKey("saId")){
							c2.add(like("saId", ((String)filters.get("saId")).trim(), MatchMode.START));
						}
						
						if(filters.containsKey("servicePointId")){
							c2.add(like("servicePointId", ((String)filters.get("servicePointId")).trim(), MatchMode.START));
						}
						
						if(filters.containsKey("premiseId")){
							c2.add(like("premiseId", ((String)filters.get("premiseId")).trim(), MatchMode.START));
						}
						
						if(filters.containsKey("acctId")){
							c2.add(like("acctId", ((String)filters.get("acctId")).trim(), MatchMode.START));
						}
						
						if(filters.containsKey("druid")){
							c2.add(like("druid", ((String)filters.get("druid")).trim(), MatchMode.START));
						}
						
						if(filters.containsKey("serviceAddress")){
							c2.add(like("serviceAddress", ((String)filters.get("serviceAddress")).trim(), MatchMode.ANYWHERE));
						}
						
						if(filters.containsKey("successfulNotification")){
							if("Success".equals((String)filters.get("successfulNotification"))){
								c2.add(eq("successfulNotification", (SuccessfulNotificationType.DELIVERED)));
							} else if("Fail".equals((String)filters.get("successfulNotification"))){
								c2.add(disjunction()
										.add(eq("successfulNotification", (SuccessfulNotificationType.ATTEMPTED)))
										.add(eq("successfulNotification", (SuccessfulNotificationType.UNSUCCESS)))
								);
							}
						}

					}
					c2.setProjection(Projections.rowCount());
					
					Long count = (Long) c2.uniqueResult();
					
			        this.setRowCount(count.intValue());
					
			        filterString = s.toString();
			        
				}
			} else {
				log.info("******************** Event is null: {}", this.event);
			}
			
		} else {
			log.info("******************** eventList is null: {}", eventList);
		}
        
        return datasource;
	}

	public List<GasPdpSrParticipant> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<GasPdpSrParticipant> datasource) {
		this.datasource = datasource;
	}
	
}
