package com.inenergis.controller.converter;

import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesConverter("phoneConverter")
public class PhoneConverter implements Converter{

    Pattern pattern = Pattern.compile("<\\w*>(\\d{10})<\\/\\w*>");

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
        String phoneNumber = (String) modelValue;
        return convertToAmericanMaskIfPhoneNumber(phoneNumber);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        // Conversion is not necessary for now. However, if you ever intend to use
        // it on input components, you probably want to implement it here.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String convertToAmericanMaskIfPhoneNumber(String phoneNumber) {
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.find()) {
            String phoneNumberExtracted = matcher.group(1);
            return phoneNumber.replace(phoneNumberExtracted,getPhoneFormatted(phoneNumberExtracted));
        }
        if(phoneNumber!=null && phoneNumber.length()==10 && StringUtils.isNumeric(phoneNumber)){
            return getPhoneFormatted(phoneNumber);
        }else{
            return phoneNumber;
        }
    }

    public String getPhoneFormatted(String phoneNumber) {
        return "("+phoneNumber.substring(0,3)+") "+phoneNumber.substring(3,6)+"-"+phoneNumber.substring(6);
    }

}