package com.inenergis.controller.converter;

import com.inenergis.entity.config.CurrencyConfig;
import com.inenergis.util.ConstantsProviderModel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import java.math.BigDecimal;
import java.math.RoundingMode;
@FacesConverter(value = "moneyCentsConverter")
public class MoneyCentsConverter extends MoneyConverter {

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        final CurrencyConfig config = currencyConfigService.getSelected();
        final BigDecimal bigDecimal = o instanceof Long? new BigDecimal((Long) o): (BigDecimal) o;
        return config.getFormatter().format(bigDecimal
                .setScale(6, RoundingMode.HALF_EVEN)
                .divide(ConstantsProviderModel.ONE_HUNDRED_BIG_DECIMAL, RoundingMode.HALF_EVEN));
       // return "";
    }


}
