package com.inenergis.entity.workflow;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.TaskStatus;
import com.inenergis.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.inenergis.util.TimeUtil.SECONDS_IN_A_DAY;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@Table(name = "PLAN_INSTANCE")
@Entity
public abstract class PlanInstance extends IdentifiableEntity {

    @Column(name = "START")
    private Date start;

    @Column(name = "ESTIMATED_END")
    private Date estimatedEnd;

    @Column(name = "ACTUAL_END")
    private Date actualEnd;

    @Column(name = "ELAPSED_TOTAL_TIME")
    private Long elapsedTotalTime;

    @Column(name = "ELAPSED_PAUSED_TIME")
    private Long elapsedPausedTime;

    @Column(name = "PAUSE_START")
    private Date pauseStart;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "LAST_PAUSE_START")
    private Date lastPauseStart;

    @Column(name = "RESUMED_TIME")
    private Date resumedTime;

    @ManyToOne
    @JoinColumn(name = "WORK_PLAN_ID")
    private WorkPlan workPlan;

    @OneToMany(mappedBy = "planInstance", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TaskInstance> taskInstances;

    public Long calculateDaysInProgress() {
        if (elapsedTotalTime != null) {
            return elapsedTotalTime / TimeUtil.SECONDS_IN_A_DAY;
        }
        Long pausedTime = this.elapsedPausedTime == null ? 0 : elapsedPausedTime;
        long days = Duration.between(start.toInstant(), Instant.now().minusSeconds(pausedTime)).toDays();
        return Math.max(days, 0L);
    }

    public Long calculateDaysInPause() {
        Long elapsedPauseTimeBefore = elapsedPausedTime != null ? elapsedPausedTime : 0;
        if (pauseStart != null) {
            elapsedPauseTimeBefore += TimeUtil.calculateTotalElapsedTime(pauseStart, new Date(), 0L);
        }
        return elapsedPauseTimeBefore / SECONDS_IN_A_DAY;
    }

    public Long calculateTotalElapsedTime() {
        return TimeUtil.calculateTotalElapsedTime(start, actualEnd, elapsedPausedTime);
    }

    public List<TaskInstance> getSortedTaskInstances() {
        return getTaskInstances() == null ? null : getTaskInstances().stream().sorted(Comparator.comparing(t -> new BigDecimal(t.getOrder()))).collect(Collectors.toList());
    }

}
