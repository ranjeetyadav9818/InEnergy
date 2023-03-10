package com.inenergis.util;

import com.inenergis.entity.History;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;
import java.math.BigDecimal;

@Named
@ApplicationScoped
public class HistoryDataTranslator {

    private static final Logger log = LoggerFactory.getLogger(PropertyAccessor.class);

    public String getField(History history) {
        return history.getField();
    }

    public String getOldValue(History history) {
        return getValue(history.getField(), history.getOldValue());
    }

    public String getNewValue(History history) {
        return getValue(history.getField(), history.getNewValue());
    }

    private String getValue(String field, String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }

        if (field.equals("defaultBidPrice") || field.equals("dualEventBidPrice")) {
            try {
                return BigDecimal.valueOf(Long.parseLong(value))
                        .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP)
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toString();
            } catch (NumberFormatException e) {
                log.error("Can't parse value for " + field, e);
            }
        }

        return value;
    }
}