package com.inenergis.service;

import com.inenergis.dao.NoteDao;
import com.inenergis.entity.Note;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class NoteService {

    private static final Logger log = LoggerFactory.getLogger(NoteService.class);

    @Inject
    NoteDao noteDao;

    public void saveNote(Note note) {
        noteDao.save(note);
    }

    public List<Note> getNotes(String entity, String entityId) {
        return noteDao.getNotes(entity, entityId);
    }
}