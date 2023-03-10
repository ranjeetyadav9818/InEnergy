package com.inenergis.microbot.camel.services;

import com.inenergis.entity.genericEnum.NotificationDefinitionId;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.microbot.camel.dao.ProgramProfileDao;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Service
public class ProgramProfileToEndService extends SystemAlertService {
    public static final int DAYS_BEFORE_END_NOTIFICATION = 15;

    @Autowired
    private ProgramProfileDao programProfileDao;

    @Override
    @Transactional
    public List searchForEntitiesToGenerateAlerts() {
        LocalDate from = ZonedDateTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDate();
        LocalDate to = from.plusDays(DAYS_BEFORE_END_NOTIFICATION);
        final Date profileEnding = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return programProfileDao.getProgramProfilesBy(profileEnding, NotificationDefinitionId.PROFILE_END_DATE_ALERT);
    }

    @Override
    protected String generateReference(Object entity) {
        ProgramProfile profile = (ProgramProfile) entity;
        return Long.toString(profile.getId());
    }

    @Override
    protected NotificationDefinitionId getType() {
        return NotificationDefinitionId.PROFILE_END_DATE_ALERT;
    }

    @Override
    public Map<String, Object> buildMsgFields(Object entity) {
        ProgramProfile profile = (ProgramProfile) entity;
        Map<String, Object> mailFields = new HashMap<>();
        mailFields.put("name", profile.getName());
        final Date effectiveStartDate = profile.getEffectiveStartDate();
        final Date effectiveEndDate = profile.getEffectiveEndDate();
        mailFields.put("startDate", effectiveStartDate == null ? StringUtils.EMPTY : ConstantsProviderModel.DATE_FORMAT.format(effectiveStartDate));
        mailFields.put("endDate", effectiveEndDate == null ? StringUtils.EMPTY : ConstantsProviderModel.DATE_FORMAT.format(effectiveEndDate));
        return mailFields;
    }
}
