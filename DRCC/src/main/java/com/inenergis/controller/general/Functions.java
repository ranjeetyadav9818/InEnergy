package com.inenergis.controller.general;

import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ApplicationScoped;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Antonio on 22/08/2017.
 */
@ApplicationScoped
public class Functions {

    private static final String EXCEPTION_ENUM_PATH = "com.inenergis.entity.locationRegistration.LocationSubmissionException.LocationSubmissionExceptionTypes.";

    public static String highlighter(String words, String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        if (StringUtils.isEmpty(words)) {
            return text;
        }
        for (String s : words.split(" ")) {
            text = text.replace(s, "<b>" + s + "</b>");
            text = text.replace(s.toUpperCase(), "<b>" + s.toUpperCase() + "</b>");
        }
        return text;
    }

    public static String translateException(String commaSeparatedText, ResourceBundle bundle) {
        if (StringUtils.isNotEmpty(commaSeparatedText)) {
            final String collect = Arrays.stream(commaSeparatedText.split(",")).map(s->s.trim()).map(s -> bundle.containsKey(EXCEPTION_ENUM_PATH + s) ? bundle.getString(EXCEPTION_ENUM_PATH + s) : s).collect(Collectors.joining(","));
            return collect;
        }
        return commaSeparatedText;
    }

}
