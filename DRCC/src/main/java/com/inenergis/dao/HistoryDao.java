package com.inenergis.dao;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.History;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.marketIntegration.IsoProfile;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.RatePlanProfile;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Transactional
public class HistoryDao extends GenericDao<History>  {

    public HistoryDao(){
        setClazz(History.class);
    }

    public static final String SELECT_HISTORY_FOR_AGREEMENT_POINT_MAP = "SELECT h FROM History h" +
            " WHERE (entity='Meter' AND entityId=:meterId) OR (entity='Premise' AND entityId=:premiseId) OR (entity='Account' AND entityId=:accountId) OR " +
            "(entity='Person' AND entityId=:personId) OR (entity='ServiceAgreement' AND entityId=:saId) OR (entity='ServicePoint' AND entityId=:spId) OR " +
            "(entity='AgreementPointMap' AND entityId LIKE :spsaId) ORDER BY creationDate desc";
    public static final String SELECT_HISTORY_FOR_PROGRAM_PROFILE = "SELECT h FROM History h" +
            " WHERE (entity='ProgramProfile' AND entityId=:profileId) ORDER BY creationDate desc";
    public static final String SELECT_HISTORY_FOR_ISO_PROFILE = "SELECT h FROM History h" +
            " WHERE (entity='IsoProfile' AND entityId=:profileId) ORDER BY creationDate desc";

    public List<History> getHistory(AgreementPointMap agreementPointMap) {
        final TypedQuery<History> query = entityManager.createQuery(SELECT_HISTORY_FOR_AGREEMENT_POINT_MAP, History.class);
        assignIds(query,agreementPointMap);
        return query.getResultList();
    }

    public List<History> getHistory(ProgramProfile profile) {
        final TypedQuery<History> query = entityManager.createQuery(SELECT_HISTORY_FOR_PROGRAM_PROFILE, History.class);
        assignIds(query,profile);
        return query.getResultList();
    }

    public List<History> getHistory(IsoProfile profile) {
        final TypedQuery<History> query = entityManager.createQuery(SELECT_HISTORY_FOR_ISO_PROFILE, History.class);
        assignIds(query,profile);
        return query.getResultList();
    }

    private void assignIds(TypedQuery<History> query, AgreementPointMap agreementPointMap) {
        query.setParameter("meterId",(agreementPointMap.getServicePoint()).getMeter().getMeterId());
        query.setParameter("premiseId",agreementPointMap.getServicePoint().getPremise().getPremiseId());
        query.setParameter("accountId",agreementPointMap.getServiceAgreement().getAccount().getAccountId());
        query.setParameter("personId",agreementPointMap.getServiceAgreement().getAccount().getPerson().getPersonId());
        query.setParameter("saId",agreementPointMap.getServiceAgreement().getServiceAgreementId());
        query.setParameter("spId",agreementPointMap.getServicePoint().getServicePointId());
        query.setParameter("spsaId","%servicePointId="+agreementPointMap.getServicePoint().getServicePointId()+
                "%serviceAgreementId="+agreementPointMap.getServiceAgreement().getServiceAgreementId()+"%");
    }

    private void assignIds(TypedQuery<History> query, ProgramProfile profile) {
        query.setParameter("profileId",profile.getId().toString());
    }

    private void assignIds(TypedQuery<History> query, IsoProfile profile) {
        query.setParameter("profileId",profile.getId().toString());
    }

    public List<History> getHistory(LocationSubmissionStatus locationSubmissionStatus) {
        return getByEntityAndId(locationSubmissionStatus.getClass().getSimpleName(), locationSubmissionStatus.getId().toString());
    }

    public List<History> getHistory(RatePlanProfile profile) {
        return getByEntityAndId(profile.getClass().getSimpleName(),profile.getId().toString());
    }

    public List<History> getByEntityAndId(String entityName, String entityId) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("entity").value(entityName).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("entityId").value(entityId).matchMode(MatchMode.EXACT).build());
        return getWithCriteria(conditions,null,"creationDate",true);
    }
}
