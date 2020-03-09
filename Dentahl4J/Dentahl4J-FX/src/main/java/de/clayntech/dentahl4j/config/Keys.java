package de.clayntech.dentahl4j.config;

import de.clayntech.config4j.Key;
import de.clayntech.config4j.impl.key.KeyFactory;

import javax.swing.*;
import java.net.URL;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public interface Keys
{

    Key<URL> REST_BASE = KeyFactory.createKey("dentahl.rest.base",URL.class);
}
