package com.inenergis.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.inenergis.util.ConstantsProviderModel;

import java.io.IOException;
import java.time.LocalDate;
/**
 * Created by egamas on 05/09/2017.
 */
public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public LocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(value.format(ConstantsProviderModel.DATE_FORMATTER));
    }
}