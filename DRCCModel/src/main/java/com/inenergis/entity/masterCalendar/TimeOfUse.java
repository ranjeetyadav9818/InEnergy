package com.inenergis.entity.masterCalendar;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.SeasonTOU;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

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
import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TIME_OF_USE")
public class TimeOfUse extends IdentifiableEntity {
    @ManyToOne
    @JoinColumn(name = "TIME_OF_USE_CALENDAR_ID")
    private TimeOfUseCalendar timeOfUseCalendar;

    @Column(name = "TOU")
    @Enumerated(EnumType.STRING)
    private SeasonTOU tou;

    @OneToMany(mappedBy = "timeOfUse", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<TimeOfUseDay> timeOfUseDays;

    @OneToMany(mappedBy = "timeOfUse", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<TimeOfUseHour> timeOfUseHours;

    public TimeOfUse() {
    }

    public TimeOfUse(TimeOfUseCalendar timeOfUseCalendar) {
        this.timeOfUseCalendar = timeOfUseCalendar;
    }

    @Transient
    public String getTOULabel() {
        final StringBuffer buffer = new StringBuffer(tou.getName());
        if (CollectionUtils.isNotEmpty(timeOfUseDays)) {
            if (timeOfUseDays.size() == 1) {
                buffer.append(" (").append(timeOfUseDays.get(0).getDay().getName()).append(")");
            }
        }
        return buffer.toString();
    }
}