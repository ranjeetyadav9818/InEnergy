package com.inenergis.entity.workflow;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "BUSINESS_OWNER")
public class BusinessOwner extends IdentifiableEntity {

	@Column(name = "NAME", length = 255)
    private String name;

    @Column(name = "EMAIL_LIST", length = 65535,columnDefinition="Text")
    private String emailList;
    
    @OneToMany(mappedBy = "businessOwner", fetch = FetchType.LAZY)
	private List<Task> tasks;

    @OneToMany(mappedBy = "businessOwner", fetch = FetchType.LAZY)
    private List<NotificationDefinition> notificationDefinitions;
    
}