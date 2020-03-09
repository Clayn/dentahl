package de.clayntech.dentahl4j.config;

import de.clayntech.config4j.Key;
import de.clayntech.config4j.impl.key.KeyFactory;

import java.net.URL;
import java.util.Locale;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public interface Keys
{

    String TASK_LOAD_TEAMS="dentahl.task.teams.load";
    String TASK_MANAGER="dentahl.taskmanager";
    Key<URL> REST_BASE = KeyFactory.createKey("dentahl.rest.base",URL.class);
    Key<Locale> LANGUAGE=new LocaleKey("dentahl.locale");
}
