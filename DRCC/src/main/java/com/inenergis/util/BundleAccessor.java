package com.inenergis.util;

import com.inenergis.entity.marketIntegration.Iso;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.PropertyResourceBundle;

/**
 * Created by egamas on 03/11/2017.
 */
@Singleton
@Named
@ApplicationScoped
@Getter
public class BundleAccessor implements Serializable {

    private PropertyResourceBundle propertyResourceBundle;
    private Locale locale;

    @PostConstruct
    public void configure() {
        FacesContext context = FacesContext.getCurrentInstance();
        locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
        propertyResourceBundle = context.getApplication().evaluateExpressionGet(context, "#{msg}", PropertyResourceBundle.class);
    }

    public void changeLocale(String baseLocale, String extendedLocale) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        locale = findLocaleByTags(baseLocale, extendedLocale);
        facesContext.getApplication().setDefaultLocale(locale);
        facesContext.getViewRoot().setLocale(locale);
        configure();
    }

    public Locale findLocaleByTags(String baseLocale, String extendedLocale) {
        final String localeToCompare = StringUtils.isNotEmpty(extendedLocale) ? extendedLocale : baseLocale;
        final Optional<Locale> first = Arrays.stream(Locale.getAvailableLocales()).filter(l -> l.toLanguageTag().replace("-", "_").equals(localeToCompare)).findFirst();
        if (!first.isPresent()) {
            final String[] languageArea = baseLocale.split("_");
            if (languageArea.length != 2) {
                return locale; // The old one
            }
            return new Locale(languageArea[0], languageArea[1] + "_" + localeToCompare);
        }
        return first.get();
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public void onLoadDiccionary(Iso iso) {
        locale = findLocaleByTags(iso.getBaseLocale(), iso.getExtendedLocale());
        changeLocale(iso.getBaseLocale(), iso.getExtendedLocale());
    }

}
