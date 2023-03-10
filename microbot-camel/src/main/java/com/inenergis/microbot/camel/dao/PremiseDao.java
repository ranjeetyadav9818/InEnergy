package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.Premise;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface PremiseDao extends Repository<Premise, Long> {

    Premise getByPremiseId(String id);
}