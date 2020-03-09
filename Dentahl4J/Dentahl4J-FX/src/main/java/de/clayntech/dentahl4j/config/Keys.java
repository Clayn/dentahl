package de.clayntech.dentahl4j.config;

import de.clayntech.config4j.Key;
import de.clayntech.config4j.impl.key.KeyFactory;

import javax.swing.*;
import java.net.URL;
import java.util.Locale;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public interface Keys
{

    Key<URL> REST_BASE = KeyFactory.createKey("dentahl.rest.base",URL.class);
    Key<Locale> LANGUAGE=new LocaleKey("dentahl.locale");
}
