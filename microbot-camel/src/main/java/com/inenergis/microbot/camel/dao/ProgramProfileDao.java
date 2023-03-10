package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.genericEnum.NotificationDefinitionId;
import com.inenergis.entity.program.ProgramProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface ProgramProfileDao extends Repository<ProgramProfile, Long> {

    @Query("select profile from ProgramProfile as profile where profile.effectiveEndDate <= :endDate " +
            "and not exists (\n" +
            "select n from NotificationInstance as n where n.reference = cast(profile.id as string) " +
            " and n.type = :type)")
    List<ProgramProfile> getProgramProfilesBy(@Param("endDate") Date date, @Param("type") NotificationDefinitionId type);
}