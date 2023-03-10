package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.Person;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface PersonDao extends Repository<Person, Long> {

    Person getByPersonId(String personId);
}