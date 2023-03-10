package com.inenergis.controller.converter;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

@FacesConverter(value = "longBigDecimalConverter")
public class LongBigDecimalConverter implements Converter {

    public static final String NAME = "longBigDecimalConverter";
    @Getter
    @Setter
    protected DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(ConstantsProviderModel.LOCALE);

    @Getter
    @Setter
    private String pattern;
    @Getter
    @Setter
    private BigDecimal factor;

    public LongBigDecimalConverter(){
        this(ConstantsProvider.THREE_DECIMAL_POSITION_PATTERN, ConstantsProviderModel.ONE_HUNDRED_BIG_DECIMAL);
    }

    public LongBigDecimalConverter(String pattern, BigDecimal factor ){
        this.pattern = pattern;
        this.factor = factor;
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        formatter.setParseBigDecimal(true);
        try {
            BigDecimal number = (BigDecimal) formatter.parse(s);
            Long longValue = number.multiply(factor).longValue();
            return longValue;
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof Long) {
            formatter.setParseBigDecimal(true);
            formatter.applyLocalizedPattern(pattern);
            Long theLong = (Long) o;
            BigDecimal theBigDecimal = new BigDecimal(theLong).divide(factor);
            formatter.setDecimalSeparatorAlwaysShown(true);
            String res = formatter.format(theBigDecimal);
            return res;
        }
        return null;
    }
}
