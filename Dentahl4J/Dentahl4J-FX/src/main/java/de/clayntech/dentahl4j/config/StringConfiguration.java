package de.clayntech.dentahl4j.config;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class StringConfiguration extends Configuration<String>
{

    public StringConfiguration(String key)
    {
        super(key);
    }

    @Override
    public String toString(String object)
    {
        return object;
    }

    @Override
    public String fromString(String string)
    {
        return string;
    }

}
