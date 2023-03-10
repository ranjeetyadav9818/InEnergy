package com.inenergis.service;

import com.inenergis.dao.DocumentDao;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.Document;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.entity.marketIntegration.IsoProfile;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.RatePlanProfile;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class DocumentService {

    @Inject
    DocumentDao documentDao;

    @Inject
    EntityManager entityManager;

    @Transactional
    public void saveDocument(Document document){
        documentDao.save(document);
    }

    public List<Document> getDocuments(IdentifiableEntity entity) {
        return documentDao.getDocuments(entity);
    }

    public void deleteDocument(Document document) {
        documentDao.delete(document);
    }
}