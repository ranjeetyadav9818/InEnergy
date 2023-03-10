package com.inenergis.entity;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import static org.apache.commons.lang.CharEncoding.UTF_8;

/**
 * Created by egamas on 30/11/2017.
 */
public abstract class DownloadableData {

    protected static final String RETURN = "\n";

    public abstract Logger getLogger();

    public abstract String getHeader();

    public ByteArrayInputStream dataToByteArrayInputStream(List<? extends Serializable> serializableList) throws SQLException, IOException {
        PrintWriter writer = null;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(out), UTF_8));
            writer.append(getHeader() + RETURN);
            for (Serializable serializable : serializableList) {
                final String serialize = serialize(serializable);
                if (StringUtils.isNotEmpty(serialize)) {
                    writer.append(serialize).append(RETURN);
                }
            }
            writer.close();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                getLogger().error("Error closing writer ", e);
            }
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    protected abstract String serialize(Serializable objectToSerialize);
}
