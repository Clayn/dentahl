package de.clayntech.dentahl4j.config;

import java.net.URL;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public interface Keys
{

    public static final Configuration<URL> REST_BASE = new URLConfiguration(
            "dentahl.rest.base");
}
