package com.inenergis.entity.maintenanceData;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.TaskStatus;
import com.inenergis.entity.workflow.TaskInstance;
import com.inenergis.entity.workflow.WorkPlan;
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
@Table(name = "MAINTENANCE_DATA")
@Entity
public abstract class MaintenanceData extends IdentifiableEntity {

    @Column(name = "VALUE_DATA")
    private String value;

}
