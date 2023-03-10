package com.inenergis.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * The persistent class for the PDP_SR_EVENT database table.
 * 
 */
@Entity
@Table(name="PDP_SR_EVENT")
@NamedQuery(name="PdpSrEvent.findAll", query="SELECT p FROM PdpSrEvent p ORDER BY p.eventStart DESC")
public class PdpSrEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long eventId;
	private Date eventEnd;
	private String eventName;
	private String eventOptions;
	private String eventProgram;
	private Date eventStart;
	private String eventState;
	private String eventType;
	private String eventUniqueId;
	private int numNotifications;
	private int numNotificationsSent;
	private int numParticipants;
	private Date filesReceived;
	private Date dedupCompleted;
	private Date vendorFilesSent;
	private Date firstPostbackReceived;
	private Date lastPostbackReceived;
	private List<PdpSrNotification> pdpSrNotifications = new ArrayList<PdpSrNotification>();
	private List<PdpSrParticipant> pdpSrParticipants = new ArrayList<PdpSrParticipant>();
//	private List<PdpSrVendor> pdpSrVendors = new ArrayList<PdpSrVendor>();
	
	private BigDecimal countDelivered;
	private BigDecimal countAttempted;

	private Map<String, List<PdpSrNotification>> notificationsByChannel;

	public PdpSrEvent() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="EVENT_ID")
	public Long getEventId() {
		return this.eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EVENT_END")
	public Date getEventEnd() {
		return this.eventEnd;
	}

	public void setEventEnd(Date eventEnd) {
		this.eventEnd = eventEnd;
	}


	@Column(name="EVENT_NAME")
	public String getEventName() {
		return this.eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	@Column(name="EVENT_OPTIONS")
	public String getEventOptions() {
		return this.eventOptions;
	}

	public void setEventOptions(String eventOptions) {
		this.eventOptions = eventOptions;
	}
	
	@Transient
	public String getEventOptionsLabel() {
		if(getEventOptions()!=null){
			StringBuilder s = new StringBuilder();
			if(getEventOptions().contains("O")){
				if(s.length()>0){
					s.append(", ");
				}
				s.append("Odd");
			} 
			
			if(getEventOptions().contains("U")){
				if(s.length()>0){
					s.append(", ");
				}
				s.append("Unlimited");
			}
			
			if(getEventOptions().contains("E")){
				if(s.length()>0){
					s.append(", ");
				}
				s.append("Even");
			}
			
			return s.toString();
		} else {
			return null;
		}
	}


	@Column(name="EVENT_PROGRAM")
	public String getEventProgram() {
		return this.eventProgram;
	}

	public void setEventProgram(String eventProgram) {
		this.eventProgram = eventProgram;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EVENT_START")
	public Date getEventStart() {
		return this.eventStart;
	}

	public void setEventStart(Date eventStart) {
		this.eventStart = eventStart;
	}


	@Column(name="EVENT_STATE")
	public String getEventState() {
		return this.eventState;
	}

	public void setEventState(String eventState) {
		this.eventState = eventState;
	}


	@Column(name="EVENT_TYPE")
	public String getEventType() {
		return this.eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}


	@Column(name="EVENT_UNIQUE_ID")
	public String getEventUniqueId() {
		return this.eventUniqueId;
	}

	public void setEventUniqueId(String eventUniqueId) {
		this.eventUniqueId = eventUniqueId;
	}


	@Column(name="NUM_NOTIFICATIONS")
	public int getNumNotifications() {
		return this.numNotifications;
	}

	public void setNumNotifications(int numNotifications) {
		this.numNotifications = numNotifications;
	}


	@Column(name="NUM_NOTIFICATIONS_SENT")
	public int getNumNotificationsSent() {
		return this.numNotificationsSent;
	}

	public void setNumNotificationsSent(int numNotificationsSent) {
		this.numNotificationsSent = numNotificationsSent;
	}


	@Column(name="NUM_PARTICIPANTS")
	public int getNumParticipants() {
		return this.numParticipants;
	}

	public void setNumParticipants(int numParticipants) {
		this.numParticipants = numParticipants;
	}


	//bi-directional many-to-one association to PdpSrNotification
	@OneToMany(mappedBy="pdpSrEvent", fetch = FetchType.LAZY)
	public List<PdpSrNotification> getPdpSrNotifications() {
		return this.pdpSrNotifications;
	}

	public void setPdpSrNotifications(List<PdpSrNotification> pdpSrNotifications) {
		this.pdpSrNotifications = pdpSrNotifications;
	}

	public PdpSrNotification addPdpSrNotification(PdpSrNotification pdpSrNotification) {
		getPdpSrNotifications().add(pdpSrNotification);
		pdpSrNotification.setPdpSrEvent(this);

		return pdpSrNotification;
	}

	public PdpSrNotification removePdpSrNotification(PdpSrNotification pdpSrNotification) {
		getPdpSrNotifications().remove(pdpSrNotification);
		pdpSrNotification.setPdpSrEvent(null);

		return pdpSrNotification;
	}


	//bi-directional many-to-one association to PdpSrParticipant
	@OneToMany(mappedBy="pdpSrEvent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<PdpSrParticipant> getPdpSrParticipants() {
		return this.pdpSrParticipants;
	}

	public void setPdpSrParticipants(List<PdpSrParticipant> pdpSrParticipants) {
		this.pdpSrParticipants = pdpSrParticipants;
	}

	public PdpSrParticipant addPdpSrParticipant(PdpSrParticipant pdpSrParticipant) {
		getPdpSrParticipants().add(pdpSrParticipant);
		pdpSrParticipant.setPdpSrEvent(this);

		return pdpSrParticipant;
	}

	public PdpSrParticipant removePdpSrParticipant(PdpSrParticipant pdpSrParticipant) {
		getPdpSrParticipants().remove(pdpSrParticipant);
		pdpSrParticipant.setPdpSrEvent(null);

		return pdpSrParticipant;
	}


	@Transient
	public BigDecimal getCountDelivered() {
		return countDelivered;
	}


	public void setCountDelivered(BigDecimal countDelivered) {
		this.countDelivered = countDelivered;
	}


	@Transient
	public BigDecimal getCountAttempted() {
		return countAttempted;
	}


	public void setCountAttempted(BigDecimal countAttempted) {
		this.countAttempted = countAttempted;
	}

	@Transient
    public Map<String, List<PdpSrNotification>> getNotificationsByChannel() {
        return notificationsByChannel;
    }

    public void setNotificationsByChannel(Map<String, List<PdpSrNotification>> notificationsByChannel) {
        this.notificationsByChannel = notificationsByChannel;
    }

    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="FILES_RECEIVED")
	public Date getFilesReceived() {
		return filesReceived;
	}


	public void setFilesReceived(Date filesReceived) {
		this.filesReceived = filesReceived;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DEDUP_COMPLETE")
	public Date getDedupCompleted() {
		return dedupCompleted;
	}


	public void setDedupCompleted(Date dedupCompleted) {
		this.dedupCompleted = dedupCompleted;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="VENDOR_FILES_SENT")
	public Date getVendorFilesSent() {
		return vendorFilesSent;
	}


	public void setVendorFilesSent(Date vendorFilesSent) {
		this.vendorFilesSent = vendorFilesSent;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FIRST_POSTBACK_RECEIVED")
	public Date getFirstPostbackReceived() {
		return firstPostbackReceived;
	}

	public void setFirstPostbackReceived(Date firstPostbackReceived) {
		this.firstPostbackReceived = firstPostbackReceived;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_POSTBACK_RECEIVED")
	public Date getLastPostbackReceived() {
		return lastPostbackReceived;
	}

	public void setLastPostbackReceived(Date lastPostbackReceived) {
		this.lastPostbackReceived = lastPostbackReceived;
	}


}