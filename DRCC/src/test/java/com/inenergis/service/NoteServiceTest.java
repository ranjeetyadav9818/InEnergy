package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.NoteDao;
import com.inenergis.entity.Note;
import org.hibernate.criterion.MatchMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class NoteServiceTest {

    @Mock
    private NoteDao noteDao;

    @Captor
    ArgumentCaptor<List<CriteriaCondition>> conditionsCaptor;

    @InjectMocks
    private NoteService noteService = new NoteService();

    @BeforeEach
    public void inject(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveANote() throws Exception {
        Note note = new Note();
        noteService.saveNote(note);
        Mockito.verify(noteDao).save(note);
    }

    @Test
    public void testSaveANoteWithError() throws Exception {
        Note note = new Note();
        Mockito.when(noteDao.save(note)).thenThrow(new NullPointerException());
        try{
            noteService.saveNote(note);
            Assertions.fail("it should throw an exception");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void testRegister() throws Exception {
        noteService.getNotes("entity", "id");
        Mockito.verify(noteDao).getNotes("entity", "id");
    }

}
