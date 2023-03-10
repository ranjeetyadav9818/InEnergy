package com.inenergis.controller.converter;

import com.inenergis.entity.config.CurrencyConfig;
import com.inenergis.service.CurrencyConfigService;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
@FacesConverter(value = "moneyConverter")
public class MoneyConverter implements Converter {

    @Setter
    @Inject
    protected CurrencyConfigService currencyConfigService;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
            final CurrencyConfig config = currencyConfigService.getSelected();
            DecimalFormat asObjectFormatter = (DecimalFormat) NumberFormat.getInstance(config.getLocale());
            String sWithoutCurrencySymbol = s.replace(config.getCurrency().getSymbol(), StringUtils.EMPTY);
            return asObjectFormatter.parse(sWithoutCurrencySymbol);
        } catch (ParseException e) {
            log.error("Error parsing currency " + s, e);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o ==null) {
            return StringUtils.EMPTY;
        }
        return currencyConfigService.getSelected().getFormatter().format(o);
    }
}
