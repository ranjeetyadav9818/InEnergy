package com.inenergis.controller.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inenergis.model.ElasticAggregator;
import com.inenergis.model.ElasticAgreementPointMap;
import com.inenergis.model.ElasticAsset;
import com.inenergis.model.ElasticContract;
import com.inenergis.model.ElasticContractEntity;
import com.inenergis.model.ElasticDevice;
import com.inenergis.model.ElasticEvent;
import com.inenergis.model.ElasticEventNotification;
import com.inenergis.model.ElasticISO;
import com.inenergis.model.ElasticInvoice;
import com.inenergis.model.ElasticLocation;
import com.inenergis.model.ElasticProgram;
import com.inenergis.model.ElasticRegistration;
import com.inenergis.model.ElasticResource;
import com.inenergis.model.SearchMatch;
import com.inenergis.model.SearchSuggestion;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Arrays;
import java.util.List;

@FacesConverter(value = "elasticObjectConverter")
public class ElasticObjectConverter implements Converter {

    private ObjectMapper mapper = new ObjectMapper();

    private final List<Class> classes = Arrays.asList(ElasticAgreementPointMap.class, ElasticAggregator.class, ElasticISO.class,
            ElasticAsset.class, ElasticDevice.class, ElasticLocation.class, ElasticResource.class, ElasticRegistration.class,
            ElasticProgram.class, ElasticEvent.class, ElasticEventNotification.class, SearchSuggestion.class,
            ElasticContractEntity.class, ElasticContract.class, ElasticInvoice.class);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        for (Class aClass : classes) {
            SearchMatch o = getObject(value, aClass);
            if (o != null) {
                if (getMap(value).get("type").asText().equals(o.getType())) {
                    return o;
                }
            }
        }
        return value;
    }

    private SearchMatch getObject(String value, Class type) {
        try {
            return (SearchMatch) mapper.readValue(value, type);
        } catch (Exception e) {
            return null;
        }
    }

    private JsonNode getMap(String value) {
        try {
            return mapper.readTree(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value.toString();
    }
}