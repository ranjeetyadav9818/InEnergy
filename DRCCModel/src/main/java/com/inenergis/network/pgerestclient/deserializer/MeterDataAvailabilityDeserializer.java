package com.inenergis.network.pgerestclient.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.inenergis.network.pgerestclient.model.MeterDataAvailabilityResponseModel;
import com.inenergis.network.pgerestclient.model.MeterDataAvailabilityResponseWrapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeterDataAvailabilityDeserializer implements JsonDeserializer<MeterDataAvailabilityResponseWrapper> {

    @Override
    public MeterDataAvailabilityResponseWrapper deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement jsonType = jsonObject.get("ArrayOfIds").getAsJsonObject().get("Header");

        Map<String, List<MeterDataAvailabilityResponseModel>> availabilities = new HashMap<>();

        Type listType = new TypeToken<ArrayList<MeterDataAvailabilityResponseModel>>() {
        }.getType();

        for (JsonElement k : jsonType.getAsJsonArray()) {
            List<MeterDataAvailabilityResponseModel> meterDataAvailabilityResponseModel = new Gson().fromJson(k.getAsJsonObject().get("Detail").toString(), listType);

            availabilities.put(k.getAsJsonObject().get("Id").getAsString(), meterDataAvailabilityResponseModel);
        }

        MeterDataAvailabilityResponseWrapper response = new MeterDataAvailabilityResponseWrapper();
        response.setAvailabilities(availabilities);

        return response;
    }
}
