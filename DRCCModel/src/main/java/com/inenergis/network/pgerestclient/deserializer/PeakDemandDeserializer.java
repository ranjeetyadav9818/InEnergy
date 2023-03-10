package com.inenergis.network.pgerestclient.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.inenergis.network.pgerestclient.model.PeakDemandResponseModel;
import com.inenergis.network.pgerestclient.model.PeakDemandResponseWrapper;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;

public class PeakDemandDeserializer implements JsonDeserializer<PeakDemandResponseWrapper> {

    @Override
    public PeakDemandResponseWrapper deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement jsonType = jsonObject.get("ArrayOfIds").getAsJsonObject().get("Header");

        Map<String, List<PeakDemandResponseModel>> availability = new HashMap<>();

        Type listType = new TypeToken<ArrayList<PeakDemandResponseModel>>() {
        }.getType();

        for (JsonElement k : jsonType.getAsJsonArray()) {
            List<PeakDemandResponseModel> responseModel = new Gson().fromJson(k.getAsJsonObject().get("Detail"), listType);
            for (PeakDemandResponseModel peakDemandResponseModel : responseModel) {
                peakDemandResponseModel.setDate(parsePartialDate(peakDemandResponseModel.getMonthYear()));
            }

            availability.put(k.getAsJsonObject().get("Id").getAsString(), responseModel);
        }

        PeakDemandResponseWrapper response = new PeakDemandResponseWrapper();
        response.setAvailabilities(availability);

        return response;
    }

    private LocalDate parsePartialDate(String partialDate) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("M/y")
                .parseDefaulting(DAY_OF_MONTH, 1)
                .toFormatter();
        return LocalDate.parse(partialDate, formatter);
    }
}
