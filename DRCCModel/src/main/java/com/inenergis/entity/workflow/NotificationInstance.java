package com.inenergis.entity.workflow;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.NotificationDefinitionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "NOTIFICATION_INSTANCE")
public class NotificationInstance extends IdentifiableEntity {

	@Column(name = "CREATED")
    private Date created;
	
	@Column(name = "CLOSED")
    private Date closed;
	
	@Column(name = "REFERENCE", length=45)
	private String reference;

    @Column(name = "NOTIFICATION_KEY", length=50)
    private String notificationKey;

    @Column(name = "SENT")
    private boolean sent;

    @Column(name = "MSG_HEADER", length = 255)
    private String messageHeader;

    @Column(name = "MSG_BODY", length = 255)
    private String messageBody;
	
	@ManyToOne
    @JoinColumn(name = "BUSINESS_OWNER_ID")
    private BusinessOwner businessOwner;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private NotificationDefinitionId type;

    @ManyToOne
    @JoinColumn(name = "TASK_INSTANCE_ID")
    private TaskInstance taskInstance;
    
}
