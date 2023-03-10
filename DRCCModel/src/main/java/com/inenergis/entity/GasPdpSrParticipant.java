package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * The persistent class for the PDP_SR_PARTICIPANTS database table.
 * 
 */
@Entity
@Table(name="GAS_PDP_SR_PARTICIPANTS")
@NamedQuery(name="GasPdpSrParticipant.findAll", query="SELECT p FROM GasPdpSrParticipant p")
public class GasPdpSrParticipant implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long participantId;
	private String acctId;
	private String druid;
	private String premiseId;
	private String saId;
	private GasServiceAgreement serviceAgreement;
	private String serviceAddress;
	private String servicePointId;
	private SuccessfulNotificationType successfulNotification;
	private List<GasPdpSrNotification> gasPdpSrNotifications = new ArrayList<GasPdpSrNotification>();
	private GasPdpSrEvent gasPdpSrEvent;

	public GasPdpSrParticipant() {

	}

	public enum SuccessfulNotificationType {
		UNSUCCESS, ATTEMPTED, DELIVERED;
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="PARTICIPANT_ID")
	public Long getParticipantId() {
		return this.participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}


	@Column(name="ACCT_ID")
	public String getAcctId() {
		return this.acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}


	@Column(name="DRUID")
	public String getDruid() {
		return this.druid;
	}

	public void setDruid(String druid) {
		this.druid = druid;
	}


	@Column(name="GAS_PREMISE_ID")
	public String getPremiseId() {
		return this.premiseId;
	}

	public void setPremiseId(String premiseId) {
		this.premiseId = premiseId;
	}

	@Column(name="SA_ID", insertable=false, updatable=false)
	public String getSaId() {
		return this.saId;
	}

	public void setSaId(String saId) {
		this.saId = saId;
	}


	@Column(name="SERVICE_ADDRESS")
	public String getServiceAddress() {
		return this.serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}


	@Column(name="SERVICE_POINT_ID")
	public String getServicePointId() {
		return this.servicePointId;
	}

	public void setServicePointId(String servicePointId) {
		this.servicePointId = servicePointId;
	}


	@Column(name="SUCCESSFUL_NOTIFICATION")
	@Enumerated(EnumType.STRING)
	public SuccessfulNotificationType getSuccessfulNotification() {
		return this.successfulNotification;
	}

	public void setSuccessfulNotification(SuccessfulNotificationType successfulNotification) {
		this.successfulNotification = successfulNotification;
	}


	//bi-directional many-to-one association to PdpSrNotification
	@OneToMany(mappedBy="pdpSrParticipant", cascade = CascadeType.ALL)
	public List<GasPdpSrNotification> getPdpSrNotifications() {
		return this.gasPdpSrNotifications;
	}

	public void setPdpSrNotifications(List<GasPdpSrNotification> gasPdpSrNotifications) {
		this.gasPdpSrNotifications = gasPdpSrNotifications;
	}

	public GasPdpSrNotification addPdpSrNotification(GasPdpSrNotification gasPdpSrNotification) {
		if(getPdpSrNotifications()==null){
		    setPdpSrNotifications(new ArrayList<>());
        }
		getPdpSrNotifications().add(gasPdpSrNotification);
		gasPdpSrNotification.setPdpSrParticipant(this);

		return gasPdpSrNotification;
	}

	public GasPdpSrNotification removePdpSrNotification(GasPdpSrNotification gasPdpSrNotification) {
		getPdpSrNotifications().remove(gasPdpSrNotification);
		gasPdpSrNotification.setPdpSrParticipant(null);

		return gasPdpSrNotification;
	}


	//bi-directional many-to-one association to PdpSrEvent
	@ManyToOne
	@JoinColumn(name="PDP_SR_EVENT_EVENT_ID")
	public GasPdpSrEvent getPdpSrEvent() {
		return this.gasPdpSrEvent;
	}

	public void setPdpSrEvent(GasPdpSrEvent gasPdpSrEvent) {
		this.gasPdpSrEvent = gasPdpSrEvent;
	}

	@Transient
	public boolean isDelivered(){
		if(SuccessfulNotificationType.DELIVERED.equals(this.getSuccessfulNotification())){
			return true;
		} else {
			return false;
		}
	}

	@Transient
	public boolean isAttempted(){
		if(SuccessfulNotificationType.ATTEMPTED.equals(this.getSuccessfulNotification())){
			return true;
		} else {
			return false;
		}
	}

	@Transient
	public boolean isUnsuccessful(){
		if(SuccessfulNotificationType.UNSUCCESS.equals(this.getSuccessfulNotification())){
			return true;
		} else {
			return false;
		}
	}

	//bi-directional many-to-one association to Service Agreement
	@ManyToOne
	@JoinColumn(name="SA_ID")
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonManagedReference
	public GasServiceAgreement getServiceAgreement() {
		return serviceAgreement;
	}

	public void setServiceAgreement(GasServiceAgreement serviceAgreement) {
		this.serviceAgreement = serviceAgreement;
	}

}
