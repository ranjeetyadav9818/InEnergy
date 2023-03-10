package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.NotificationType;
import com.inenergis.entity.genericEnum.Vendor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Table(name = "NOTIFICATION_DUPLICATION_SOURCE")
@ToString(of = {"notificationType","distributedBy"})
public class NotificationDuplicationSource extends IdentifiableEntity {

    @Column(name = "NOTIFICATION_TYPE")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "DISTRIBUTED_BY")
    @Enumerated(EnumType.STRING)
    private Vendor distributedBy;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_CUSTOMER_NOTIFICATION_ID")
    private ProgramCustomerNotification programCustomerNotification;
}