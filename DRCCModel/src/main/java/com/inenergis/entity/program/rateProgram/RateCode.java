package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.ActivityStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(of = "name")
@Entity
@EqualsAndHashCode(of = {"name"}, callSuper = false)
@Table(name = "RATE_CODE")
public class RateCode extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "RATE_STATUS")
    @Enumerated(EnumType.STRING)
    private ActivityStatus rateStatus;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @OneToMany(mappedBy = "rateCode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RateCodeSectors> sectors;

}