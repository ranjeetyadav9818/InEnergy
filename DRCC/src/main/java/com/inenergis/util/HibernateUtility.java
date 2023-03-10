package com.inenergis.util;

import javax.persistence.EntityManager;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class HibernateUtility {
	
	@SuppressWarnings("unchecked")
	public static <T> T initializeAndUnproxy(T entity) {
	    if (entity == null) {
	        throw new
	           NullPointerException("Entity passed for initialization is null");
	    }
	    if (entity instanceof HibernateProxy) {
	    	Hibernate.initialize(entity);
	        entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
	                .getImplementation();
	    }
	    return entity;
	}
	
//	@SuppressWarnings("unchecked")
//	public static <T> T initializeAndUnproxy(EntityManager em, Class c, Object o) {		
//	    if (o == null) {
//	        throw new
//	           NullPointerException("Entity passed for initialization is null");
//	    }
//	    if (em == null) {
//	        throw new
//	           NullPointerException("Entity passed for initialization is null");
//	    }
//	    if (entity instanceof HibernateProxy) {
//	    	Hibernate.initialize(entity);
//	        entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
//	                .getImplementation();
//	    }
//	    return entity;
//	}
	
}
