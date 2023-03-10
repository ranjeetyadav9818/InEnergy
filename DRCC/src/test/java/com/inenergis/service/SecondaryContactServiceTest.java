package com.inenergis.service;

import com.inenergis.dao.SecondaryContactDao;
import com.inenergis.entity.SecondaryContact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class SecondaryContactServiceTest {

    @Mock
    private SecondaryContactDao secondaryContactDao;

    @InjectMocks
    private SecondaryContactService secondaryContactService;

    private SecondaryContact secondaryContact;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        secondaryContact = new SecondaryContact();
    }

    @Test
    void saveOrUpdate() {
        secondaryContactService.saveOrUpdate(secondaryContact);
        Mockito.verify(secondaryContactDao).saveOrUpdate(secondaryContact);
    }

    @Test
    void delete() {
        secondaryContactService.delete(secondaryContact);
        Mockito.verify(secondaryContactDao).delete(secondaryContact);
    }

    @Test
    void getAll() {
        secondaryContactService.getAll();
        Mockito.verify(secondaryContactDao).getAll();
    }

    @Test
    void getById() {
        secondaryContactService.getById(2L);
        Mockito.verify(secondaryContactDao).getById(2L);
    }
}