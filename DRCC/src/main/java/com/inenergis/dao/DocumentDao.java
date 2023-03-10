package com.inenergis.dao;

import com.inenergis.entity.Document;
import com.inenergis.entity.IdentifiableEntity;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Transactional
public class DocumentDao extends GenericDao<Document>  {

    public DocumentDao(){
        setClazz(Document.class);
    }

    public List<Document> getDocuments(IdentifiableEntity entity) {
        return getByEntityAndId(entity.getClass().getName(), entity.getId().toString());
    }

    public List<Document> getByEntityAndId(String entityName, String entityId) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("entity").value(entityName).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("entityId").value(entityId).matchMode(MatchMode.EXACT).build());
        return getWithCriteria(conditions,null,"creationDate",true);
    }
}
