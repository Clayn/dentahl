package de.clayntech.dentahl4j.config;

import javafx.util.StringConverter;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @param <T>
 * @since 0.1
 */
public abstract class Configuration<T> extends StringConverter<T>
{

    private final String key;

    public Configuration(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }

}
