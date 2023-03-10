package com.inenergis.dao;

import com.inenergis.entity.award.Instruction;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class InstructionDao extends GenericDao<Instruction> {
    public InstructionDao(){setClazz(Instruction.class);}
}
