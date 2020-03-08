package de.clayntech.dentahl4j.config;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class IntegerConfiguration extends Configuration<Integer>
{

    public IntegerConfiguration(String key)
    {
        super(key);
    }

    @Override
    public String toString(Integer object)
    {
        return object == null ? "" : object.toString();
    }

    @Override
    public Integer fromString(String string)
    {
        return string == null || string.isEmpty() ? null : Integer.valueOf(
                string);
    }

}
