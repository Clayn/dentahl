package de.clayntech.dentahl4j;

import de.clayntech.dentahl4j.fx.MainApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Launcher {
    private static final Set<String> RESOURCE_PATHS=new HashSet<>(Arrays.asList("i18n/dialog.properties","i18n/language.properties","dentahl.default.properties","logback.xml","app.properties"));
    private static final Logger LOG= LoggerFactory.getLogger(Launcher.class);
    private static final Consumer<String> PRINTER=System.out::println;
    private static void checkFile(String file) {
        String path=file;
        if(!path.startsWith("/")) {
            path="/"+path;
        }
        message("Checking resource file: "+path);
        URL u=Launcher.class.getResource(path);
        message("Found: "+u);
        if(u==null) {
            throw new IllegalStateException("Resource "+path+" not available");
        }
    }
    private static void message(String mes) {
        LOG.debug(mes);
        PRINTER.accept(mes);
    }
    public static void main(String[] args) {
        RESOURCE_PATHS.stream().forEach(Launcher::checkFile);
        MainApp.main(args);
    }
}
