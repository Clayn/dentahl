package de.clayntech.dentahl4j.server.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.clayntech.config4j.Config4J;
import de.clayntech.config4j.Configuration;
import de.clayntech.config4j.Key;
import de.clayntech.config4j.impl.key.KeyFactory;
import de.clayntech.dentahl4j.domain.Ninja;
import de.clayntech.dentahl4j.server.db.NinjaRepository;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.tags.form.InputTag;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
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

    public List<Ninja> grabNinjas() throws IOException {
        List<Ninja> ninjas=new ArrayList<>();
        String url="https://en.konohaproxy.com.br/ugc1/getHuoyingData/dataen";
        URL u=new URL(url);
        URLConnection con=u.openConnection();
        String json="";
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        try(InputStream in=con.getInputStream();ByteArrayOutputStream bout=new ByteArrayOutputStream()) {
            int read=-1;
            byte[] buffer=new byte[128];
            while((read=in.read(buffer))!=-1) {
                bout.write(buffer,0,read);
            }
            bout.flush();
            json=new String(bout.toByteArray());
        }

        Gson gson=new Gson();
        JsonObject jobject=gson.fromJson(json,JsonObject.class);
        if(jobject.has("data")) {
           JsonObject inner=jobject.getAsJsonObject("data");
           if(inner.has("ninjas")) {
               JsonArray arr=inner.getAsJsonArray("ninjas");
               for(int i=0;i<arr.size();++i) {
                   JsonObject obj=arr.get(i).getAsJsonObject();
                   if(obj.has("szPicUrl")) {
                       if(obj.has("iNid")) {
                           PojoNinja pn=gson.fromJson(arr.get(i),PojoNinja.class);
                           if(doLog) {
                               LOG.info("Grabbed ninja: {}", pn.getSzName());
                           }
                           Ninja n=pn.toNinja("https://en.konohaproxy.com.br/include/images/ninja/","https://en.konohaproxy.com.br/include/images/ninja2/");
                           if(n.getMain()==0) {
                               ninjas.add(n);
                           }
                       }
                   }
               }
           }
        }
        if(doLog) {
            LOG.info("Grabbed {} ninjas", ninjas.size());
        }
        return ninjas;
    }
}
