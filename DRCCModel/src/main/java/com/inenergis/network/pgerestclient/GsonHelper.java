package com.inenergis.network.pgerestclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.inenergis.network.pgerestclient.deserializer.MeterDataAvailabilityDeserializer;
import com.inenergis.network.pgerestclient.deserializer.PeakDemandDeserializer;
import com.inenergis.network.pgerestclient.model.MeterDataAvailabilityResponseWrapper;
import com.inenergis.network.pgerestclient.model.PeakDemandResponseWrapper;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonHelper {

    private static transient Gson gson;

    static {
        JsonSerializer<LocalDateTime> ser = new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                return src == null ? null : new JsonPrimitive(src.withNano(0).format(DateTimeFormatter.ISO_DATE_TIME));
            }
        };

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, ser)
                .registerTypeAdapter(MeterDataAvailabilityResponseWrapper.class, new MeterDataAvailabilityDeserializer())
                .registerTypeAdapter(PeakDemandResponseWrapper.class, new PeakDemandDeserializer())
                .create();
    }

    public static Gson getGson() {
        return gson;
    }
}
