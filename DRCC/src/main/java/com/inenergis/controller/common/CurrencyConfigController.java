package com.inenergis.controller.common;

import com.inenergis.controller.converter.LongBigDecimalConverter;
import com.inenergis.entity.config.CurrencyConfig;
import com.inenergis.model.ElasticInvoice;
import com.inenergis.model.SearchMatch;
import com.inenergis.service.CurrencyConfigService;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.convert.BigDecimalConverter;
import javax.faces.convert.Converter;
import javax.faces.convert.DoubleConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Named
@ViewScoped
@Getter
@Setter
public class CurrencyConfigController implements Serializable {

    @Inject
    private CurrencyConfigService currencyConfigService;

    private CurrencyConfig currencyConfig;

    @PostConstruct
    public void init() {
        currencyConfig = currencyConfigService.getSelected();
    }

    public boolean isNegative(Number number) {
        if (number == null) {
            return false;
        }
        if (number.getClass().isAssignableFrom(BigDecimal.class)) {
            return ((BigDecimal) number).compareTo(BigDecimal.ZERO) <= -1;
        } else if (number.getClass().isAssignableFrom(Double.class)) {
            return ((Double) number).compareTo(0D) <= -1;
        } else if (number.getClass().isAssignableFrom(Long.class)) {
            return ((Long) number).compareTo(0L) <= -1;
        }
        return false;
    }

    public boolean isSymbolBefore() {
        if (currencyConfig != null) {
            return ((DecimalFormat) currencyConfig.getFormatter()).toLocalizedPattern().indexOf("¤") <= 0;
        }
        return false;
    }

    public Converter getConverterForInput(Number value) {
        if (value == null){
            return new BigDecimalConverter();
        } else if (value instanceof BigDecimal) {
            return new BigDecimalConverter();
        } else if (value instanceof Double) {
            return new DoubleConverter();
        }
        return new LongBigDecimalConverter();
    }

    public String getSymbol() {
        return currencyConfig.getCurrency().getSymbol();
    }

    public String formatCurrency(SearchMatch searchMatch) {
        if (searchMatch instanceof ElasticInvoice) {
            ((ElasticInvoice) searchMatch).setFormatter(currencyConfig.getFormatter());
        }
        return searchMatch.toString();
    }

}
