package com.inenergis.controller.customerData;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.Person;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Named
@SessionScoped
public class GasCustomerSearchNavigator implements Serializable {

    private static final int RESULTS_SIZE = 10;
    private static final String RESULTS_SORT_FIELD = "name";

    private int startIndex = 0;

    private LazyDataModel<AgreementPointMap> agreementPointMapLazyModel;

    private Set<Person> customers = new HashSet<>();

    void setAgreementPointMapLazyModel(LazyDataModel<AgreementPointMap> agreementPointMapLazyModel) {
        startIndex = 0;
        this.agreementPointMapLazyModel = agreementPointMapLazyModel;
        loadData();
    }

    public void loadNext() {
        startIndex += RESULTS_SIZE;
        loadData();
    }

    public void loadPrevious() {
        startIndex -= RESULTS_SIZE;
        loadData();
    }

    private void loadData() {
        List<AgreementPointMap> agreementPointMaps = agreementPointMapLazyModel.load(startIndex, RESULTS_SIZE, RESULTS_SORT_FIELD, SortOrder.ASCENDING, new HashMap<>());

        customers = agreementPointMaps.stream()
                .map(agreementPointMap -> agreementPointMap.getServiceAgreement().getAccount().getPerson())
                .collect(Collectors.toSet());
    }

    public Set<Person> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Person> customers) {
        this.customers = customers;
    }
}