package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.program.Program;

import javax.persistence.EntityManager;
import java.util.Map;

public class LazyProgramModel extends LazyIdentifiableEntityDataModel<Program> {
    public LazyProgramModel(EntityManager entityManager, Map<String, Object> preFilters) {
        super(entityManager, Program.class, preFilters);
    }
}