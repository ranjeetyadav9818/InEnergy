package com.inenergis.entity;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DRCCBeanUtilsTest {

    //TODO include junit and annotate it
    public static void testPersonChangesHistoryCreation() throws NoSuchFieldException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        Person person1 = new Person();
        Person person2 = new Person();
        setFields(person1);
        setFields(person2);
        person2.setBusinessName("ooo");
        person2.setBusinessOwner("sdgfwe");
        person2.setCustomerName("oiwowe");
        person2.getAccounts().add(new Account());
        final List<History> histories = DRCCBeanUtils.generateHistoryFromDifferences(person1, person2, Collections.singletonList("customerName"), Collections.singletonList("businessOwner"), "personId", "author");
        System.out.println(histories);
    }

    private static void setFields(Person person1) {
        person1.setBusinessName("be");
        person1.setBusinessOwner("bo");
        person1.setCustomerName("cu");
        person1.setPersonId("id");
        person1.setPhoneExtension("pe");
        person1.setAccounts(new ArrayList<Account>());
    }
}
