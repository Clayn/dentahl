package net.bplaced.clayn.d4j.config;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class URLConfiguration extends Configuration<URL>
{

    public URLConfiguration(String key)
    {
        super(key);
    }

    @Override
    public String toString(URL object)
    {
        return object == null ? "" : object.toString();
    }

    @Override
    public URL fromString(String string)
    {
        try
        {
            return string == null || string.isEmpty() ? null : new URL(string);
        } catch (MalformedURLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

}
