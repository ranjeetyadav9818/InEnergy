package com.inenergis.entity;


import com.inenergis.entity.program.ProgramOption;

import javax.persistence.Transient;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.inenergis.entity.History.HistoryChangeType.RELATIONSHIP;
import static com.inenergis.entity.History.HistoryChangeType.FIELD;

public final class DRCCBeanUtils {


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm a");
    private static Map<String,PropertyDescriptor[]> descriptorsCache = new HashMap<>();

    public static List<History> generateHistoryFromDifferences(Object entity, Object entity2, List<String> relationshipIds, List<String> exclude, String idField, String author)
            throws IllegalAccessException, NoSuchFieldException, IntrospectionException, InvocationTargetException {
        return generateHistoryFromDifferences(entity,entity2,relationshipIds,exclude,idField,author, false);
    }
    public static List<History> generateHistoryFromDifferences(Object entity, Object entity2, List<String> relationshipIds, List<String> exclude, String idField, String author, boolean followCollections)
            throws IllegalAccessException, NoSuchFieldException, IntrospectionException, InvocationTargetException {
        Class entityClazz = entity.getClass();
        List<History> result = new ArrayList<>();

        if(relationshipIds == null){
            relationshipIds= new ArrayList<>();
        }
        if(exclude == null){
            exclude = new ArrayList<>();
        }

        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(entityClazz);
        String entityId = getEntityId(entity,idField,propertyDescriptors);
        for (int i = 0; i < propertyDescriptors.length; i++) {
            final PropertyDescriptor prop = propertyDescriptors[i];
            if(valueChanged(prop,exclude,entity,entity2)){
                final History.HistoryChangeType changeType = relationshipIds.contains(prop.getName()) ? RELATIONSHIP : FIELD;
                final String entity1Value = getValue(prop.getReadMethod().invoke(entity));
                final String entity2Value = getValue(prop.getReadMethod().invoke(entity2));
                History history = createHistoryEntity(entityId, entityClazz.getSimpleName(), prop.getName(), entity1Value, entity2Value, changeType, author);
                result.add(history);
            }
            if(Collection.class.isAssignableFrom(prop.getPropertyType()) && followCollections && propertyIsAvailableForChecking(prop,exclude,entity)){
                result.addAll(getHistoryChangesFromCollection(prop,exclude,entity,entity2, entityId, entityClazz.getSimpleName(),prop.getName(), author));
            }
        }
        return result;
    }

    private static History createHistoryEntity(String entityId, String entityName, String field, String oldValue, String newValue, History.HistoryChangeType changeType, String author) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        History history = new History();
        history.setCreationDate(new Date());
        history.setEntity(entityName);
        history.setEntityId(entityId);
        history.setField(field);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        history.setChangeType(changeType);
        history.setAuthor(author);
        return history;
    }

    private static List<History> generateHistoryFromEqualsObjects(Object o1, Object o2, String field, String entityName, String entityId, String author) throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        List<History> result = new ArrayList<History>();
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(o1.getClass());
        for (int i = 0; i < propertyDescriptors.length; i++) {
            final PropertyDescriptor prop = propertyDescriptors[i];
            List<String> exclude = new ArrayList<String>();
            final HistoryTracked annotation = o1.getClass().getAnnotation(HistoryTracked.class);
            if(annotation !=null) {
                exclude = Arrays.asList(annotation.notCheck());
            }
            if(valueChanged(prop,exclude,o1,o2)){
                History history = createHistoryEntity(entityId, entityName, field + " -> " + prop.getName(), getValue(prop.getReadMethod().invoke(o1)), getValue(prop.getReadMethod().invoke(o2)), FIELD, author);
                result.add(history);
            }
            if(Collection.class.isAssignableFrom(prop.getPropertyType())){
                result.addAll(getHistoryChangesFromCollection(prop,exclude,o1,o2, entityId, entityName, field + " -> " + prop.getName(), author));
            }
        }
        return result;
    }

    private static List<History> getHistoryChangesFromCollection(PropertyDescriptor propertyDescriptor, List<String> exclude, Object entity, Object entity2, String entityId, String entityName, String field, String author) throws InvocationTargetException, IllegalAccessException, IntrospectionException, NoSuchFieldException {
        List<History> result = new ArrayList<History>();
        if(!exclude.contains(propertyDescriptor.getName())){
            final Collection collection1 = ((Collection) propertyDescriptor.getReadMethod().invoke(entity));
            final Collection collection2 = ((Collection) propertyDescriptor.getReadMethod().invoke(entity2));
            if(collection1 == null || collection1.size() == 0){
                if(collection2!=null && collection2.size()>0){
                    result.addAll(allNewObjects(collection2, field, entityName, entityId, author));
                }
            }else{
                if(collection2 ==null || collection2.size() == 0){
                    result.addAll(allDeletedObjects(collection1, field , entityName, entityId, author));
                }else{
                    result.addAll(compareObjectsInCollection(collection1,collection2, field , entityName, entityId, author));
                }
            }
        }
        return result;
    }

    private static Collection<? extends History> compareObjectsInCollection(Collection collection1, Collection collection2, String field, String entityName, String entityId, String author) throws IllegalAccessException, IntrospectionException, InvocationTargetException, NoSuchFieldException {
        List<History> result= new ArrayList<History>();
        final Iterator iterator1 = collection1.iterator();
        while (iterator1.hasNext()){
            final Object next = iterator1.next();
            if(next!=null && IdentifiableEntity.class.isAssignableFrom(next.getClass())){
                if(!collection2.contains(next)){
                    result.add(generateHistoryFromDeletedObject(((IdentifiableEntity) next), field,entityName,entityId, author));
                }else{
                    result.addAll(generateHistoryFromEqualsObjects(next,findObjectInCollection(next,collection2), field,entityName,entityId, author));
                }
            }

        }
        final Iterator iterator2 = collection2.iterator();
        while (iterator2.hasNext()){
            final Object next = iterator2.next();
            if(next!=null && IdentifiableEntity.class.isAssignableFrom(next.getClass())){
                if(!collection1.contains(next)){
                    result.add(generateHistoryFromNewObject(((IdentifiableEntity) next), field,entityName,entityId, author));
                }
            }
        }
        return result;
    }

    private static Object findObjectInCollection(Object next, Collection collection) {
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()){
            final Object nextFromCollection = iterator.next();
            if(next.equals(nextFromCollection)){
                return nextFromCollection;
            }
        }
        return null;
    }

    private static Collection<? extends History> allDeletedObjects(Collection collection, String field, String entityName, String entityId, String author) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        List<History> result= new ArrayList<History>();
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()){
            final Object next = iterator.next();
            if(next!=null && IdentifiableEntity.class.isAssignableFrom(next.getClass())){
                result.add(generateHistoryFromDeletedObject(((IdentifiableEntity) next), field, entityName, entityId, author));
            }
        }
        return result;
    }

    private static List<History> allNewObjects(Collection collection, String field, String entityName, String entityId, String author) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        List<History> result= new ArrayList<History>();
        final Iterator iterator = collection.iterator();
        while (iterator.hasNext()){
            final Object next = iterator.next();
            if(next!=null && IdentifiableEntity.class.isAssignableFrom(next.getClass())){
                result.add(generateHistoryFromNewObject(((IdentifiableEntity) next), field,entityName,entityId, author));
            }
        }
        return result;
    }

    private static History generateHistoryFromDeletedObject(IdentifiableEntity entity, String field, String entityName, String id, String author) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        History history = createHistoryEntity(id, entityName, field, getValue(entity), null, FIELD, author);
        return history;
    }

    private static History generateHistoryFromNewObject(IdentifiableEntity entity, String field, String entityName, String id, String author) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        History history = createHistoryEntity(id, entityName, field, null, getValue(entity), FIELD, author);
        return history;
    }

    private static String getEntityId(Object copy1, String idField, PropertyDescriptor[] propertyDescriptors) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
        for (int i = 0; i < propertyDescriptors.length; i++) {
            if(propertyDescriptors[i].getName().equalsIgnoreCase(idField)){
                return getValue(propertyDescriptors[i].getReadMethod().invoke(copy1));
            }
        }
        return null;
    }

    private static boolean valueChanged(PropertyDescriptor propertyDescriptor, List<String> exclude, Object entity, Object entity2) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        if (propertyDescriptor!=null) {
            if( !Collection.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && !Map.class.isAssignableFrom(propertyDescriptor.getPropertyType())){
                if(propertyIsAvailableForChecking(propertyDescriptor, exclude, entity)){
                    if(!same(propertyDescriptor.getReadMethod().invoke(entity), propertyDescriptor.getReadMethod().invoke(entity2))){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean propertyIsAvailableForChecking(PropertyDescriptor propertyDescriptor, List<String> exclude, Object entity) throws NoSuchFieldException {
        return !exclude.contains(propertyDescriptor.getName()) && !isAnnotatedByTransient(propertyDescriptor, entity);
    }

    private static boolean isAnnotatedByTransient(PropertyDescriptor propertyDescriptor, Object entity) throws NoSuchFieldException {
        try{
            return entity.getClass().getDeclaredField(propertyDescriptor.getName()).isAnnotationPresent(Transient.class);
        }catch (Exception e){
            return true;
        }
    }

    private static boolean same(Object o, Object o1) {
        if (o == o1) return true;
        if (o == null){
            return false;
        }
        if(o1 == null) return false;
        if(Date.class.isAssignableFrom(o.getClass())){
            Date d1 = (Date) o;
            Date d2 = (Date) o1;
            return d1.getTime() == d2.getTime();
        }
        if(BigDecimal.class.isAssignableFrom(o.getClass()) && BigDecimal.class.isAssignableFrom(o1.getClass())){
            return ((BigDecimal) o1).setScale(((BigDecimal) o).scale()).equals(o);
        }
        return o.equals(o1);
    }

    private static String getValue(Object o) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if(o==null){
            return "";
        }else{
            if(o instanceof VModelEntity){
                VModelEntity vModel = (VModelEntity) o;
                final PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(vModel.getClass()).getPropertyDescriptors();
                for (int i = 0; i < propertyDescriptors.length; i++) {
                    if(propertyDescriptors[i].getName().equalsIgnoreCase(vModel.idFieldName())){
                        return String.valueOf(propertyDescriptors[i].getReadMethod().invoke(vModel));
                    }
                }
                return vModel.toString();
            } else if(Date.class.isAssignableFrom(o.getClass())){
                return sdf.format(((Date) o));
            } else {
                return o.toString();
            }
        }
    }

    private static PropertyDescriptor[] getPropertyDescriptors(Class<? extends Object> clazz) throws IntrospectionException {
        PropertyDescriptor[] propertyDescriptors = descriptorsCache.get(clazz.getCanonicalName());
        if(propertyDescriptors == null){
            propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            descriptorsCache.put(clazz.getCanonicalName(),propertyDescriptors);
        }
        return propertyDescriptors;
    }

    public static void main(String ... args) throws IntrospectionException, NoSuchFieldException {
        ProgramOption o1 = new ProgramOption();
        final HistoryTracked annotation = o1.getClass().getAnnotation(HistoryTracked.class);
        if(annotation !=null){
            System.out.println(annotation.notCheck());
        }
    }
}










