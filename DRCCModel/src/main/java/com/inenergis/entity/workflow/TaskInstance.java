package com.inenergis.entity.workflow;


import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TASK_INSTANCE")
public class TaskInstance extends IdentifiableEntity {

    public static final int TASK_ORDER_SCALE = 4;

    @Column(name = "START")
    private Date start;

    @Column(name = "ESTIMATED_END")
    private Date estimatedEnd;

    @Column(name = "ACTUAL_END")
    private Date actualEnd;

    @Column(name = "ELAPSED_TIME")
    private Long elapsedTime;

    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    @Column(name = "STATUS", length = 45)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "NOTES", length = 65535, columnDefinition = "Text")
    private String notes;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "TASK_ORDER", length = 20)
    private String order;

    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "PLAN_INSTANCE_ID")
    private PlanInstance planInstance;

    @OneToMany(mappedBy = "taskInstance", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NotificationInstance> notificationInstanceList;

    public Pair<Integer, Integer> getParsedOrder() {
        if (order == null) {
            order = StringUtils.EMPTY;
        }
        String trimmedOrder = order.trim();
        String[] parts = trimmedOrder.split("\\.");
        String intPart = StringUtils.EMPTY;
        String decPart = StringUtils.EMPTY;
        if (parts.length > 0) {
            intPart = parts[0];
        }
        if (parts.length > 1) {
            decPart = parts[1];
        }
        String rPaddedDec = (StringUtils.rightPad(decPart, TASK_ORDER_SCALE, '0').substring(0, TASK_ORDER_SCALE));

        Integer integerPart = StringUtils.EMPTY.equals(intPart) ? 0 : Integer.parseInt(intPart);
        Integer decimalPart = StringUtils.EMPTY.equals(rPaddedDec) ? 0 : Integer.parseInt(rPaddedDec);

        return ImmutablePair.of(integerPart, decimalPart);
    }

    public Long calculateDaysPastDue(){
        if(actualEnd != null){
            return null;
        }
        if(estimatedEnd == null){
            return null;
        }
        long days = Duration.between(estimatedEnd.toInstant(), Instant.now()).toDays();
        return Math.max(days,0);
    }

    public void addNotificationInstanceList(NotificationInstance notificationInstance) {
        if(notificationInstanceList == null){
            notificationInstanceList = new ArrayList<>();
        }
        notificationInstanceList.add(notificationInstance);
    }
}
