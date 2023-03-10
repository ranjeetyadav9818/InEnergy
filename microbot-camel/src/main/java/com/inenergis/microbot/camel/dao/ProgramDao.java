package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.program.Program;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface ProgramDao extends Repository<Program, Long> {

    Program getByName(String name);
}