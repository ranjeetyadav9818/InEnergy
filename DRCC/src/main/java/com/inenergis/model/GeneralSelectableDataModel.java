package com.inenergis.model;


import com.inenergis.entity.IdentifiableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.model.IterableDataModel;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.DataModel;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class GeneralSelectableDataModel extends DataModel<IdentifiableEntity> implements SelectableDataModel<IdentifiableEntity>, Serializable  {

    private int rowIndex = -1;
    private List<? extends IdentifiableEntity> list;

    public GeneralSelectableDataModel(List<? extends IdentifiableEntity> list) {
        this.list = list;
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
    public IdentifiableEntity getRowData() {
        if (list == null) {
            return null;
        }
        if (!isRowAvailable()) {
            throw new IllegalStateException();
        }

        return list.get(rowIndex);
    }

    @Override
    public Object getWrappedData() {
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setWrappedData(Object data) {
        if (data == null) {
            list = null;
            setRowIndex(-1);
        } else {
            list = ((List<? extends IdentifiableEntity>) data);
            setRowIndex(0);
        }
    }

    private IdentifiableEntity getRowDataOrNull() {
        if (isRowAvailable()) {
            return getRowData();
        }
        return null;
    }


    @Override
    public Object getRowKey(IdentifiableEntity object) {
        return object.getUuid();
    }

    @Override
    public IdentifiableEntity getRowData(String rowKey) {
        for (IdentifiableEntity identifiableEntity : list) {
            if(identifiableEntity.getUuid().equals(rowKey)){
                return identifiableEntity;
            }
        }
        return null;
    }
}
