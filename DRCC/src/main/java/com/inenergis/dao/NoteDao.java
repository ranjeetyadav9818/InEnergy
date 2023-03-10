package com.inenergis.dao;

import com.inenergis.entity.Note;
import com.inenergis.entity.program.ProgramProfile;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Transactional
public class NoteDao extends GenericDao<Note> {

    public NoteDao() {
        setClazz(Note.class);
    }

    public List<Note> getNotes(String entity, String entityId) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("entity").value(entity).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("entityId").value(entityId).matchMode(MatchMode.EXACT).build());
        return getWithCriteria(conditions);
    }
}
