package com.inenergis.service;

import com.inenergis.dao.InstructionDao;
import com.inenergis.entity.award.Instruction;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class InstructionService {

    @Inject
    InstructionDao instructionDao;


    public void save(Instruction instruction) {
        instructionDao.save(instruction);
    }

    public List<Instruction> getAll() {
        return instructionDao.getAll();
    }

    public Instruction getById(Long id) {
        return instructionDao.getById(id);
    }
}
