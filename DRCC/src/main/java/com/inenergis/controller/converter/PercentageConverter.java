package com.inenergis.controller.converter;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.util.ConstantsProviderModel;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import java.math.BigDecimal;
import java.text.ParseException;

@FacesConverter(value = "percentageConverter")
public class PercentageConverter extends LongBigDecimalConverter {

    public static final String NAME = "percentageConverter";
    public static final String PERCENTAGE = "%";

    public PercentageConverter(){
        super(ConstantsProvider.TWO_DECIMAL_POSITION_PATTERN, ConstantsProviderModel.ONE_HUNDRED_BIG_DECIMAL);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        String strLongBigDecimal = super.getAsString(facesContext,uiComponent,o);
        return strLongBigDecimal+ PERCENTAGE;
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        String sWithoutPercentage = s.replace("%", StringUtils.EMPTY);
        return super.getAsObject(facesContext,uiComponent,sWithoutPercentage);
    }
}
