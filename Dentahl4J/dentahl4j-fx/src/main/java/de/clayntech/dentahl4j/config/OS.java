package de.clayntech.dentahl4j.config;

import java.io.File;

public interface OS {
    static boolean isWindows() {
        return System.getProperty("os.name","").toLowerCase().contains("windows");
    }

    static String getOSUserHome() {
        return new File(
                isWindows() ? System.getenv().getOrDefault("APPDATA", System.getProperty("user.home")) : System.getProperty("user.home"), "Dentahl4J"
        ).getAbsolutePath();
    }

    static File getOSDirectory(String directory) {
        return new File(getOSUserHome(),directory);
    }
}
