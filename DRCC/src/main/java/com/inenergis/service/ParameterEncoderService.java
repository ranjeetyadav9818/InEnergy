package com.inenergis.service;


import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.util.UIMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.util.Base64;

import javax.ejb.Stateless;
import javax.inject.Named;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@Named
public class ParameterEncoderService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    public static final String D = "d";

    public static String encodeDate(Date date) {
        return encode(sdf.format(date));
    }

    public static String encode(String s) {
        String r = org.primefaces.util.Base64.encodeToString(s.getBytes(), false);
        return r;
    }

    public static List<String> encode(String s , String v) {
        List<String> p = new ArrayList<>();
        String b = org.primefaces.util.Base64.encodeToString(v.getBytes(), false);
        String r = org.primefaces.util.Base64.encodeToString(s.getBytes(), false);
        p.add(b);
        p.add(r);
        return p;
    }

    public static String decode(String s) {
        if (s == null) {
            return null;
        }
        return new String(Base64.decode(s));
    }

    public static String getDecodedParameter(String parameterName) {
        String parameterValue = PrimeFacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parameterName);
        if (StringUtils.isEmpty(parameterValue)) {
            return null;
        }
        return decode(parameterValue);
    }

    public static Date getDecodedDateParameter(String prameterName) {
        String sDate = getDecodedParameter(prameterName);
        if (!StringUtils.isEmpty(sDate)) {
            try {
                return sdf.parse(decode(sDate));
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    public static String getDefaultDecodedParameter() {
        return getDecodedParameter("o");
    }

    public static String getDecodedParameter() {

        return getDecodedParameter("c");
    }

    public static Long getDecodedParameterAsLong(String param) {
        String decodedParameter = getDecodedParameter(param);
        if (StringUtils.isNotEmpty(decodedParameter) && NumberUtils.isNumber(decodedParameter)) {
            return Long.parseLong(decodedParameter);
        }
        return null;
    }
    public static Long getDefaultDecodedParameterAsLong() {
        return getDecodedParameterAsLong("o");
    }

    public static String encode(Object eventId) {

        return (encode(eventId.toString()));
    }

    public static Date getTradeDateParameter(UIMessage uiMessage) {
        Date tradeDate = null;// = Date.from(LocalDate.now().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        String sTradeDate = ParameterEncoderService.getDecodedParameter(D);
        if (!org.apache.commons.lang3.StringUtils.isEmpty(sTradeDate)) {
            try {
                tradeDate = ConstantsProvider.DATE_FORMAT.parse(sTradeDate);
            } catch (ParseException e) {
                tradeDate = new Date();
                uiMessage.addMessage("Wrong Date Format. Must be: " + ConstantsProvider.MM_DD_YYYY);
            }
        }
        return tradeDate;
    }

    public static Iso retrieveIsoFromParameter(IsoService isoService, UIMessage uiMessage) {
        Long isoId = ParameterEncoderService.getDecodedParameterAsLong("o");
        if (isoId == null) {
            uiMessage.addMessage("Market Id required");
            return null;
        }
        Iso iso = isoService.getIso(isoId);
        if (iso == null) {
            uiMessage.addMessage("Market Id not found");
        }
        return iso;
    }

    public static CommodityType getCommodityTypeParameter(){
        final String commodityTypeParam = getDecodedParameter("n");
        if (StringUtils.isNotEmpty(commodityTypeParam)) {
             return CommodityType.valueOf(commodityTypeParam);
        }
        return null;
    }
}