package net.bplaced.clayn.d4j.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class DentahlConfiguration
{

    private static final Path DEFAULT_LOCATION = new File("dentahl.properties").toPath();
    private static final DentahlConfiguration CONFIG = new DentahlConfiguration();

    private final Properties properties = new Properties();

    private DentahlConfiguration()
    {
    }

    public static DentahlConfiguration getConfiguration()
    {
        return CONFIG;
    }

    public void load(InputStream in) throws IOException
    {
        properties.clear();
        properties.load(in);
    }

    public void load() throws IOException
    {
        if (!Files.exists(DEFAULT_LOCATION))
        {
            return;
        }
        try (InputStream in = Files.newInputStream(DEFAULT_LOCATION))
        {
            load(in);
        }
    }

    public void store(OutputStream out) throws IOException
    {
        properties.store(out, "");
        out.flush();
    }

    public void store() throws IOException
    {
        if (!Files.exists(DEFAULT_LOCATION))
        {
            Files.createFile(DEFAULT_LOCATION);
        }
        try (OutputStream out = Files.newOutputStream(DEFAULT_LOCATION))
        {
            store(out);
        }
    }

    public boolean isSet(String key)
    {
        return get(key, null) != null;
    }

    public boolean isSet(Configuration<?> key)
    {
        return get(key, null) != null;
    }

    public void set(String key, String val)
    {
        properties.setProperty(key, val);
    }

    public String get(String key, String def)
    {
        return properties.getProperty(key, def);
    }

    public String get(String key)
    {
        return properties.getProperty(key, null);
    }

    public <T> void set(Configuration<T> key, T val)
    {
        set(key.getKey(), key.toString(val));
    }

    public <T> T get(Configuration<T> key, T def)
    {
        String val = get(key.getKey());
        return val == null ? def : key.fromString(val);
    }

    public <T> T get(Configuration<T> key)
    {
        return get(key, null);
    }
}
