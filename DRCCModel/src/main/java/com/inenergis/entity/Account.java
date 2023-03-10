package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * The persistent class for the ACCOUNT database table.
 * 
 */
@Entity
@Table(name="ACCOUNT")
@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a")
public class Account implements Serializable,VModelEntity {
	private static final long serialVersionUID = 1L;
	private String accountId;
	private Person person;
	private List<BaseServiceAgreement> serviceAgreements;

	public Account() {
	}


	@Id
	@Column(name="ACCOUNT_ID")
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="PERSON_ID")
	@JsonManagedReference
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}


	//bi-directional many-to-one association to BaseServiceAgreement
	@OneToMany(mappedBy="account")
	@JsonBackReference
	public List<BaseServiceAgreement> getServiceAgreements() {
		return this.serviceAgreements;
	}

	public void setServiceAgreements(List<BaseServiceAgreement> serviceAgreements) {
		this.serviceAgreements = serviceAgreements;
	}

	public BaseServiceAgreement addServiceAgreement(BaseServiceAgreement serviceAgreement) {
		getServiceAgreements().add(serviceAgreement);
		serviceAgreement.setAccount(this);

		return serviceAgreement;
	}

	public BaseServiceAgreement removeServiceAgreement(BaseServiceAgreement serviceAgreement) {
		getServiceAgreements().remove(serviceAgreement);
		serviceAgreement.setAccount(null);

		return serviceAgreement;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Account account = (Account) o;

		if (accountId != null ? !accountId.equals(account.accountId) : account.accountId != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return accountId != null ? accountId.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Account{" +
				"accountId='" + accountId + '\'' +
				", person=" + person +
				'}';
	}

	public String idFieldName() {
		return "accountId";
	}

	public List<String> relationshipFieldNames() {
		return Arrays.asList("person");
	}

	public List<String> excludedFieldsToCompare() {
		return null;
	}
}