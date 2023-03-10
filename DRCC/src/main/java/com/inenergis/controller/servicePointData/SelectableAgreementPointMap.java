package com.inenergis.controller.servicePointData;

import com.inenergis.entity.AgreementPointMap;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.DataModel;
import java.util.List;


public class SelectableAgreementPointMap extends DataModel implements SelectableDataModel<AgreementPointMap> {

    //TODO TEMPORARY CLASS TO BUGFIX USE GeneralSelectableDataModelInstead (AgreementPointMap should extend IdentifiableEntity)
    List<AgreementPointMap> list;
    private int rowIndex = -1;

    public SelectableAgreementPointMap(List<AgreementPointMap> datasource) {

        this.list = datasource;
        if (list != null && !list.isEmpty()){
            rowIndex=0;
        }
    }

    @Override
    public AgreementPointMap getRowData(String rowKey) {
        for (AgreementPointMap amp : list) {
            if (amp.getRowKey().equals(rowKey)) {
                return amp;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(AgreementPointMap amp) {
        return amp.getRowKey();
    }


    @Override
    public boolean isRowAvailable() {
        return list != null && rowIndex >= 0 && rowIndex < list.size();
    }

    @Override
    public int getRowCount() {
        if (list == null) {
            return -1;
        }

        return list.size();
    }

    @Override
    public Object getRowData() {
        if (list == null) {
            return null;
        }
        if (!isRowAvailable()) {
            throw new IllegalStateException();
        }

        return list.get(rowIndex);
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public void setRowIndex(int i) {
        this.rowIndex = i;
    }

    @Override
    public Object getWrappedData() {
        return list;
    }

    @Override
    public void setWrappedData(Object data) {
        if (data == null) {
            list = null;
            setRowIndex(-1);
        } else {
            list = ((List) data);
            setRowIndex(0);
        }
    }
}
