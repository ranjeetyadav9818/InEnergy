package com.inenergis.entity.workflow;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.NotificationDefinitionId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "NOTIFICATION_DEFINITION")
public class NotificationDefinition extends IdentifiableEntity {

	@Column(name = "TYPE", length = 45)
    @Enumerated(EnumType.STRING)
    private NotificationDefinitionId type;
	
	@Column(name = "DESCRIPTION", length = 255)
    private String description;
	
	@ManyToOne
    @JoinColumn(name = "BUSINESS_OWNER_ID")
    private BusinessOwner businessOwner;
    
}
