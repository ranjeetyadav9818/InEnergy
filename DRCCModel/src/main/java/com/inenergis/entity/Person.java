package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the CUSTOMER database table.
 */
@Entity
@Table(name = "PERSON")
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
public class Person implements Serializable, VModelEntity {
    private static final long serialVersionUID = 1L;
    private String personId;
    private String businessName;
    private String customerName;
    private String phoneExtension;
    private String businessOwner;
    private List<Account> accounts;
    private List<CustomerNotificationPreference> customerNotificationPreferences = new ArrayList<>();

    public Person() {
    }


    @Id
    @Column(name = "PERSON_ID")
    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }


    @Column(name = "BUSINESS_NAME")
    public String getBusinessName() {
        return this.businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }


    @Column(name = "CUSTOMER_NAME")
    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "PHONE_EXTENSION")
    public String getPhoneExtension() {
        return phoneExtension;
    }


    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    @Column(name = "BUS_OWNER")
    public String getBusinessOwner() {
        return businessOwner;
    }


    public void setBusinessOwner(String businessOwner) {
        this.businessOwner = businessOwner;
    }


    //bi-directional many-to-one association to Account
    @OneToMany(mappedBy = "person")
    @JsonBackReference
    public List<Account> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account addAccount(Account account) {
        getAccounts().add(account);
        account.setPerson(this);

        return account;
    }

    public Account removeAccount(Account account) {
        getAccounts().remove(account);
        account.setPerson(null);

        return account;
    }


    //bi-directional many-to-one association to PdpSrNotification
    @OneToMany(mappedBy = "person")
    @JsonIgnore
    public List<CustomerNotificationPreference> getCustomerNotificationPreferences() {
        return customerNotificationPreferences;
    }


    public void setCustomerNotificationPreferences(List<CustomerNotificationPreference> customerNotificationPreferences) {
        this.customerNotificationPreferences = customerNotificationPreferences;
    }

    public String idFieldName() {
        return "personId";
    }

    public List<String> relationshipFieldNames() {
        return null;
    }

    public List<String> excludedFieldsToCompare() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return personId != null ? personId.equals(person.personId) : person.personId == null;
    }

    @Override
    public int hashCode() {
        return personId != null ? personId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", businessName='" + businessName + '\'' +
                ", customerName='" + customerName + '\'' +
                ", phoneExtension='" + phoneExtension + '\'' +
                ", businessOwner='" + businessOwner + '\'' +
                '}';
    }

    public String toName() {
        String name;

        if (customerName == null) {
            name = businessName;
        } else {
            name = customerName;
        }

        return name;
    }

    public String toShortName() {
        String name = toName();

        if (name.length() > 15) {
            name = name.substring(0, 15);
        }

        return name;
    }
}