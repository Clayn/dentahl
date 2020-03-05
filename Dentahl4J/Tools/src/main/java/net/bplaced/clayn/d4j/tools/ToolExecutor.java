/*
 * The MIT License
 *
 * Copyright 2018 Clayn <clayn_osmato@gmx.de>.
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
package net.bplaced.clayn.d4j.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;
import net.bplaced.clayn.d4j.domain.Element;
import net.bplaced.clayn.d4j.domain.Ninja;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class ToolExecutor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
       boolean urlOnly=true;
       String chDriver="/Users/lpazderski/Tmp/dentahl/geckodriver";
       System.setProperty("webdriver.gecko.driver", chDriver);
       String url="https://en.konohaproxy.com.br/";
            
        File f = new File("data");
        f.mkdirs();
        File dest=new File(f, "ninjas.html");
        if(!dest.exists()) {
        WebDriver driver = new FirefoxDriver();
            driver.get(url);
            BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Waiting for input");
            //reader.readLine();
            String str = null;
            do{
                Thread.sleep(500);
                System.out.println("Checking for completion");
                str=driver.getPageSource();
            }while(!str.contains("Shisui"));
            System.out.println("Page loaded. Wait a few seconds just to be sure");
            Thread.sleep(2000);
            str=driver.getPageSource();
            driver.close();
            try(FileWriter writer=new FileWriter(dest)) {
                writer.write(str);
                writer.flush();
            }
            boolean test=true;
            if(test) {
                return;
            }
        }
        List<Ninja> nins = DataExtractor.extractNinjas(null);
        System.out.println("");
        System.out.println("---------------------------");
        System.out.println("Found Ninjas: ");
        for(Ninja n:nins) {
            System.out.format("Name: %s, Element: %s \n", n.getName(),n.getElementType()!=null?n.getElementType().name():"Not found");
           
        }
        File all = new File(f, "all.sql");
        if (!all.exists()) {
            all.createNewFile();
        }
        try (BufferedWriter writer = Files.newBufferedWriter(all.toPath())) {
            for(Element el:Element.values()) {
                System.out.println(el);
                File nFile = new File(f,el.name() + ".element");
                String statement=DBHelper.toInsertStatement(el, nFile);
                writer.write(statement);
                writer.newLine();
            }
            for (Ninja n : nins) {
                System.out.println(n);
                File nFile = new File(f, n.getId() + ".ninja");
                String statement=DBHelper.toInsertStatement(n, nFile);
                writer.write(statement);
                writer.newLine();
            }
            
            writer.flush();
        }
    }

}
