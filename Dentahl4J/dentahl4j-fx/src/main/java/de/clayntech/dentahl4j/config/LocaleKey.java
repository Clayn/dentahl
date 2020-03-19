package de.clayntech.dentahl4j.config;

import de.clayntech.config4j.Key;
import de.clayntech.config4j.ValueParsingException;

import java.util.Locale;

public class LocaleKey extends Key<Locale> {

    public LocaleKey(String key) {
        super(key);
    }

    @Override
    public Locale fromString(String s) throws ValueParsingException {
        if(s==null||s.trim().isEmpty()) {
            return null;
        }
        return Locale.forLanguageTag(s);
    }

    @Override
    public String toString(Locale locale) throws ValueParsingException {
        return locale==null?"":locale.getLanguage();
    }

    @Override
    protected Class<Locale> getType() {
        return Locale.class;
    }


}
