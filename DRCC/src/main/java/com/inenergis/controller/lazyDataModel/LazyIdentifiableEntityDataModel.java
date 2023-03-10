package com.inenergis.controller.lazyDataModel;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.DateRange;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.inenergis.dao.GenericDao.addAlias;
import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.like;

@Transactional
@Getter
@Setter
public abstract class LazyIdentifiableEntityDataModel<T extends IdentifiableEntity> extends LazyDataModel<T> {

    public LazyIdentifiableEntityDataModel(EntityManager entityManager, Class clazz) {
        this(entityManager, clazz, new HashMap<String, Object>());
    }

    public LazyIdentifiableEntityDataModel(EntityManager entityManager, Class clazz, Map<String, Object> permanentFilters) {
        this.entityManager = entityManager;
        this.clazz = clazz;
        this.permanentFilters = permanentFilters;
    }

    private Class clazz;
    private EntityManager entityManager;
    private Map<String, Object> permanentFilters;

    private Logger log = LoggerFactory.getLogger(LazyIdentifiableEntityDataModel.class);

    private List<T> datasource = new ArrayList<>();


    @Override
    public T getRowData(String rowKey) {
        for (T entity : datasource) {
            if (entity.getUuid().equals(rowKey)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(T entity) {
        return entity.getUuid();
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        Session session = (Session) entityManager.getDelegate();
        Criteria criteriaData = session.createCriteria(clazz);
        Criteria criteriaCount = session.createCriteria(clazz);

        criteriaData.setFirstResult(first);
        criteriaData.setMaxResults(pageSize);
        filters.putAll(permanentFilters);
        Set<String> aliases = addFilters(criteriaData, filters);
        addOrder(criteriaData, sortField, sortOrder, aliases);
        datasource = criteriaData.list();

        //rowCount
        setRowCount(sortField, sortOrder, filters, criteriaCount, datasource,pageSize, first);
        return datasource;
    }

    protected void setRowCount(String sortField, SortOrder sortOrder, Map<String, Object> filters, Criteria criteriaCount, List<T> datasource, int pageSize, int first) {
        criteriaCount.setProjection(Projections.rowCount());
        Set<String> aliasesCount = addFilters(criteriaCount, filters);
        addOrder(criteriaCount, sortField, sortOrder, aliasesCount);//It could look silly to order in a count but sometimes order can change the number of results (nullable foreign keys)
        Long count = (Long) criteriaCount.uniqueResult();
        this.setRowCount(count.intValue());
    }

    protected void addOrder(Criteria criteria, String sortField, SortOrder sortOrder, Set<String> aliases) {
        if (sortField != null && sortOrder != null) {
            String key = addAlias(criteria, sortField, aliases);
            if (sortOrder == SortOrder.DESCENDING) {
                criteria.addOrder(Order.desc(key));
            } else {
                criteria.addOrder(Order.asc(key));
            }
        } else {
            criteria.addOrder(Order.desc("id"));
        }
    }

    protected Set<String> addFilters(Criteria criteria, Map<String, Object> filters) {
        Set<String> aliases = new HashSet<>();
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String key = addAlias(criteria, entry.getKey(), aliases);
            addFilter(criteria, key, entry);
        }
        return aliases;
    }

    protected void addFilter(Criteria criteria, String key, Map.Entry<String, Object> entry) {
        if(entry.getValue() instanceof DateRange){
            updateCriteriaForDateRange(criteria, key, (DateRange) entry.getValue());
        }else{
            switch (getComparisonMethod(entry)) {
                case LIKE:
                    criteria.add(like(key, getFilterValue(entry).toString(), getMatchMode(entry.getKey())));
                    break;
                case EXACT:
                    if (Collection.class.isAssignableFrom(entry.getValue().getClass())) {
                        if (!((Collection) entry.getValue()).isEmpty()) {
                            criteria.add(in(key, (Collection) entry.getValue()));
                        }
                    } else {
                        criteria.add(eq(key, getFilterValue(entry)));
                    }
                    break;
            }
        }
    }

    protected ComparisonMethod getComparisonMethod(Map.Entry<String, Object> entry) {
        if (isFixedValueOrCollection(entry)) {
            return ComparisonMethod.EXACT;
        }
        final String[] split = entry.getKey().split("\\.");
        if (split[split.length - 1].equalsIgnoreCase("id")) {
            return ComparisonMethod.EXACT;
        } else {
            return ComparisonMethod.LIKE;
        }
    }

    protected Object getFilterValue(Map.Entry<String, Object> entry) {
        if (isFixedValueOrCollection(entry)) {
            return entry.getValue();
        }
        final String s = entry.getValue().toString();
        if (entry.getKey().equalsIgnoreCase("id") || entry.getKey().endsWith(".id")) {
            return Long.valueOf(s);
        } else {
            return s.trim();
        }
    }

    private boolean isFixedValueOrCollection(Map.Entry<String, Object> entry) {
        return entry.getValue() instanceof Boolean || entry.getValue() instanceof Integer || entry.getValue() instanceof Long || entry.getValue() instanceof Enum || Collection.class.isAssignableFrom(entry.getValue().getClass());
    }

    protected MatchMode getMatchMode(String filter) {
        return MatchMode.START;
    }

    public enum ComparisonMethod {
        LIKE, EXACT
    }


    private String pattern = ConstantsProviderModel.MM_DD_YYYY;
    private ZoneId zoneId = ConstantsProviderModel.CUSTOMER_TIMEZONE_ID;

    public void updateCriteriaForDateRange(Criteria criteria, String fieldName, DateRange dateRange) {
        final Date from = dateRange.getFrom();
        if (from != null) {
            final LocalDateTime ldtFrom = LocalDateTime.from(from.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
            final Date dbFrom = Date.from(ldtFrom.toLocalDate().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
            if (dateRange.isLocalDateCompare()) {
                criteria.add(and(Restrictions.ge(fieldName, LocalDate.from(ldtFrom))));
            } else {
                criteria.add(and(Restrictions.ge(fieldName, dbFrom)));
            }
        }
        final Date to = dateRange.getTo();
        if (to != null) {
            final LocalDateTime ldtTo = LocalDateTime.from(to.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
            final Date dbTo = Date.from(ldtTo.toLocalDate().plusDays(1).atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
            if (dateRange.isLocalDateCompare()) {
                criteria.add(and(Restrictions.le(fieldName, LocalDate.from(ldtTo))));
            } else {
                criteria.add(and(Restrictions.le(fieldName, dbTo)));
            }
        }
    }
}