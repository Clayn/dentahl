package de.clayntech.dentahl4j.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Version {
    private static final Properties APP_PROPERTIES=new Properties();
    private static final Logger LOG= LoggerFactory.getLogger(Version.class);

    static {
        try(InputStream input=Version.class.getResourceAsStream("/app.properties")) {
            APP_PROPERTIES.load(input);
        } catch (IOException e) {
            LOG.error("Failed to load the app.properties");
        }
    }

    public static String getVersion() {
        return APP_PROPERTIES.getProperty("dentahl.version");
    }
}
