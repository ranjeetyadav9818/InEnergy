package com.inenergis.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.inenergis.util.ConstantsProviderModel;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by egamas on 05/09/2017.
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    protected LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return LocalDateTime.parse(parser.readValueAs(String.class), ConstantsProviderModel.DATE_TIME_FORMATTER);
    }
}