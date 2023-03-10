package com.inenergis.commonServices;

import com.inenergis.entity.DataMapping;
import com.inenergis.entity.DataMappingType;

import java.util.List;

/**
 * Created by egamas on 13/10/2017.
 */
public interface DataMappingServiceContract {
    String getDestinationBy(DataMappingType type, String source);

    public void delete(DataMapping dataMapping);

    public List<DataMapping> getAll();

    public List<DataMapping> getByType(DataMappingType type);

    public DataMapping getById(Long id);

    public void saveOrUpdate(DataMapping dataMapping);
}