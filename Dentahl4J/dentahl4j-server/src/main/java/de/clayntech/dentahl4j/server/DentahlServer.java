/*
 * The MIT License
 *
 * Copyright 2019 Clayn <clayn_osmato@gmx.de>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.clayntech.dentahl4j.server;

import de.clayntech.config4j.Config4J;
import de.clayntech.config4j.Configuration;
import de.clayntech.config4j.spring.Config4JBeanFactory;
import de.clayntech.config4j.util.Config4JFileParser;
import de.clayntech.dentahl4j.domain.Team;
import de.clayntech.dentahl4j.server.data.Grabber;
import de.clayntech.dentahl4j.server.db.TeamRepository;
import de.clayntech.dentahl4j.server.err.ExitCode;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
@SpringBootApplication(scanBasePackages = {"de.clayntech"})
@EnableAutoConfiguration
public class DentahlServer implements CommandLineRunner
{
    private static final Logger LOG= LoggerFactory.getLogger(DentahlServer.class);
    private static ApplicationContext context;
    private static String pidFile="dentahl.pid";

    //@Autowired
    //private Grabber grabber;

    @Autowired
    private Configuration configuration;

    @Autowired
    private TeamRepository repo;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        URL data=DentahlServer.class.getResource("/data.sql");
        URL appProp=DentahlServer.class.getResource("/application.properties");
        if(data==null&&appProp==null) {
            LOG.error("Missing data and properties file");
            System.exit(ExitCode.FILES_MISSING);
        }else if(data==null) {
            LOG.error("Missing data file");
            System.exit(ExitCode.DATA_MISSING);
        }else if(appProp==null) {
            LOG.error("Missing properties files");
            System.exit(ExitCode.APPLICATION_PROPERTIES_MISSNG);
        }
        new DentahlServer().checkPid();
        context=SpringApplication.run(DentahlServer.class, args);
    }

    private void checkPid() throws Exception {
        File pFile=new File(pidFile);
        if(pFile.exists()) {
            LOG.error("It seems that there is already an instance of the Dentahl Server running");
            System.exit(ExitCode.SERVER_ALREADY_RUNNING);
        }
        long pid=ProcessHandle.current().pid();
        Files.createFile(pFile.toPath());
        LOG.debug("Creating temporary pid file for pid {} at {}",pid,pFile.getAbsolutePath());
        pFile.deleteOnExit();
        try(FileWriter fw=new FileWriter(pFile)) {
            fw.write(""+pid);
            fw.flush();
        }
    }

    @Override
    public void run(String... args) throws Exception
    {
    }
    
}
