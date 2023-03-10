package com.inenergis.dao;

import com.inenergis.entity.program.ProgramProfile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ge;
import static org.hibernate.criterion.Restrictions.gt;
import static org.hibernate.criterion.Restrictions.isNull;
import static org.hibernate.criterion.Restrictions.lt;
import static org.hibernate.criterion.Restrictions.ne;
import static org.hibernate.criterion.Restrictions.or;

@Stateless
@Transactional
public class ProgramProfileDao extends GenericDao<ProgramProfile> {

    public ProgramProfileDao() {
        setClazz(ProgramProfile.class);
    }

    @SuppressWarnings("unchecked")
    public List<ProgramProfile> getOverlapped(ProgramProfile programProfile) {
        Date startDate = programProfile.getEffectiveStartDate();
        Date endDate = programProfile.getEffectiveEndDate();

        if (endDate != null) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault())
                    .toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
            endDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }

        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(ProgramProfile.class);
        criteria.add(eq("program", programProfile.getProgram()));
        criteria.add(ne("id", programProfile.getId()));

        Criterion criterion;
        if (endDate != null) {
            criterion = and(
                    lt("effectiveStartDate", endDate),
                    or(ge("effectiveEndDate", startDate), isNull("effectiveEndDate")));
        } else {
            criterion = or(
                    gt("effectiveEndDate", startDate),
                    isNull("effectiveEndDate"));
        }

        criteria.add(criterion);

        return criteria.list();
    }
}