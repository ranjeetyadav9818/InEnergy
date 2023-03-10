package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.locationRegistration.LocationChangelog;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public interface LocationChangelogDao extends Repository<LocationChangelog, Long> {

    @Query("select cl from LocationChangelog cl " +
            "where cl.processed = false and " +
            "cl.effectiveDate <= :threshold and " +
            "concat(year(cl.effectiveDate),month(cl.effectiveDate),day(cl.effectiveDate) )" +
            " = (select min(concat(year(cl.effectiveDate),month(cl.effectiveDate),day(cl.effectiveDate) ))" +
            " from LocationChangelog " +
            " where processed = false)"
    )
    List<LocationChangelog> getNextChangeLogs(@Param("threshold") Date threshold);

    @Modifying
    @Transactional
    @Query("update LocationChangelog set processed = true where id in :changelogs")
    void setToProcessed(@Param("changelogs") List<Long> changelogs);

    LocationChangelog findByEffectiveDateAndTypeAndLocation(Date effectiveDate, LocationChangelog.LocationChangelogType type, LocationSubmissionStatus location);

    LocationChangelog save(LocationChangelog locationChangelog);
}