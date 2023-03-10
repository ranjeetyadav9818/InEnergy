package com.inenergis.util;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.picketlink.annotations.PicketLink;

public class EntityManagerProducer {
	
	@PersistenceContext(unitName = "primary",type=PersistenceContextType.EXTENDED)
	private EntityManager entityManager;
	
	
	
	
		@Produces
		@Default
		@Dependent
		public EntityManager getEntityManager(){
			return entityManager;
		}
	
	 	@Produces
	    @PicketLink
	    public EntityManager getPicketLinkEntityManager() {
	        return entityManager;
	    }

}
