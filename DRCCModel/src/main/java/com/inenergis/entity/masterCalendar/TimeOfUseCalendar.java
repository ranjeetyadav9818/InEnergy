package com.inenergis.entity.masterCalendar;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.SeasonCalendarType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TIME_OF_USE_CALENDAR")
public class TimeOfUseCalendar extends IdentifiableEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private SeasonCalendarType type;

    @Column(name = "DATE_FROM")
    private LocalDate dateFrom;

    @Column(name = "DATE_TO")
    private LocalDate dateTo;

    @OneToMany(mappedBy = "timeOfUseCalendar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<TimeOfUse> timeOfUses;

    @Transient
    private boolean expandedRow = false;
}