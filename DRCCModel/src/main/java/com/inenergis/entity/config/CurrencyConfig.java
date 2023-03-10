package com.inenergis.entity.config;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.util.CacheAll;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "CURRENCY_CONFIG")
//@CacheAll
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CurrencyConfig extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "SELECTED")
    private boolean selected;

    @Column(name = "LOCALE")
    private String localeName;

    @Transient
    private Locale locale;

    @Transient
    private Currency currency;

    @Transient
    private NumberFormat formatter;

    @PostLoad
    public void init() {
        currency = Currency.getInstance(name);
        locale = findLocaleByTags(localeName);
        formatter = NumberFormat.getCurrencyInstance(locale);
        formatter.setMinimumFractionDigits(currency.getDefaultFractionDigits());
        formatter.setMaximumFractionDigits(6);
    }
    public Locale findLocaleByTags(String baseLocale) {
        final Optional<Locale> first = Arrays.stream(Locale.getAvailableLocales()).filter(l -> l.toLanguageTag().replace("-", "_").equals(baseLocale)).findFirst();
        if (!first.isPresent()) {
            final String[] languageArea = baseLocale.split("_");
            return new Locale(languageArea[0], languageArea[1]);
        }
        return first.get();
    }
}
