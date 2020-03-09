package de.clayntech.dentahl4j.server.data;

import de.clayntech.config4j.Config4J;
import de.clayntech.config4j.Configuration;
import de.clayntech.config4j.Key;
import de.clayntech.config4j.impl.key.KeyFactory;
import de.clayntech.dentahl4j.domain.Ninja;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Grabber {

    private static final Logger LOG= LoggerFactory.getLogger(Grabber.class);
    private static final Key<Boolean> DO_LOG_KEY= KeyFactory.createKey("dentahl.grabber.log",boolean.class);

    private boolean doLog=false;
    {
        doLog=LOG.isInfoEnabled()&&Config4J.getConfiguration().get(DO_LOG_KEY,false);
    }

    public boolean isAvailable() {
        File kon=new File(System.getProperty("user.dir"),"/dentahl/konoha.html");
        Configuration configuration=Config4J.getConfiguration();
        String firefox=configuration.get("dentahl.grabber.ff.binary","");
        String driver=configuration.get("dentahl.grabber.ff.driver","");
        LOG.info("Checking firefox: '{}' and driver '{}'",firefox,driver);
        if(firefox==null||driver==null||firefox.isBlank()||driver.isBlank()) {

            LOG.info("Checking file: {}",kon);
            return kon.exists();
        }
        if(!Files.exists(Paths.get(firefox))||!Files.exists(Paths.get(driver))) {
            LOG.info("Checking file: {}",kon);
            return kon.exists();
        }
        return true;
    }

    public List<Ninja> grabNinjas() throws IOException {
        if(!isAvailable()) {
            return Collections.emptyList();
        }
        File kon=new File(System.getProperty("user.dir"),"/dentahl/konoha.html");
        String str="";
        List<Ninja> ninjas = new ArrayList<>();
        if(kon.exists()) {
            LOG.info("Reading from: {}",kon);
            str=Files.readString(kon.toPath(),Charset.defaultCharset());
        }else {
            Configuration configuration = Config4J.getConfiguration();
            String firefox = configuration.get("dentahl.grabber.ff.binary");
            String driver = configuration.get("dentahl.grabber.ff.driver");
            FirefoxBinary bin = new FirefoxBinary();
            System.setProperty("webdriver.gecko.driver", driver);
            String url = "https://en.konohaproxy.com.br/";
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setBinary(bin);
            firefoxOptions.setHeadless(true);
            FirefoxDriver ffDriver = new FirefoxDriver(firefoxOptions);
            ffDriver.get(url);
            try {
                do {
                    Thread.sleep(500);
                    str = ffDriver.getPageSource();
                } while (!str.contains("Shisui"));
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                LOG.error("The ninja loading was interrupted", ex);
                return Collections.emptyList();
            }
            str = ffDriver.getPageSource();
            ffDriver.close();
        }
        ninjas.addAll(DataExtractor.extractNinjas(new ByteArrayInputStream(str.getBytes(Charset.defaultCharset()))));
        if(kon.exists()) {
            //kon.delete();
        }
        return ninjas;
    }
}
