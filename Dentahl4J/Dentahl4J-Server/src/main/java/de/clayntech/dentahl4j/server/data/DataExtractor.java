package de.clayntech.dentahl4j.server.data;

import de.clayntech.dentahl4j.domain.Element;
import de.clayntech.dentahl4j.domain.Ninja;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class DataExtractor {

    private static final Map<String, Element> ICON_URLS = new HashMap<>();

    static {
        String tmp = "<span class=\"attr\"><img src=\"/assets/images/icons/elements/de/feng_icon.png\" width=\"23\" height=\"24\"></span>";
        ICON_URLS.put("include/images/sim/huo_icon.png",
                Element.FIRE);
        ICON_URLS.put("include/images/sim/feng_icon.png",
                Element.WIND);
        ICON_URLS.put("include/images/sim/lei_icon.png",
                Element.LIGHTNING);
        ICON_URLS.put("include/images/sim/tu_icon.png",
                Element.EARTH);
        ICON_URLS.put("include/images/sim/shui_icon.png",
                Element.WATER);
    }
    private static final Pattern IMG_PATTERN = Pattern.compile(
            "img src=\"([^\"]*)\"");
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "alt=\"([^\"]*)\"");
    private static final String SEARCH = "<span class=\"pic\">";
    private static final int SEARCH_SIZE = SEARCH.length();
    private static final String SEARCH_END = "</span>";
    private static final String SEARCH_ELEMENT = "<span class=\"attr\">";
    private static final int SEARCH_SIZE_ELEMENT = SEARCH.length();
    private static final String SEARCH_END_ELEMENT = "</span>";
    private static final int SEARCH_END_SIZE = SEARCH_END.length();
    private static final String URL_BASE="https://en.konohaproxy.com.br/";

    public static List<Ninja> extractNinjas(InputStream in) throws IOException {
        List<Ninja> ninjas = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
        }
        String html = builder.toString();
        Set<Integer> siteIds=new HashSet<>();
        int index = 0;
        int endIndex = 0;
        int elementIndex = 0;
        int endElementIndex = 0;
        int stop = html.indexOf("btnAutoCombo");
        int id=1;
        while ((index = html.indexOf(SEARCH, index)) >= 0) {
            if (stop >= 0 && index >= stop) {
                break;
            }
            String elementInfo = null;
            endIndex = html.indexOf(SEARCH_END, index + SEARCH_SIZE);
            elementIndex = html.indexOf(SEARCH_ELEMENT, elementIndex);
            if (elementIndex >= 0) {
                endElementIndex = html.indexOf(SEARCH_END_ELEMENT,
                        elementIndex + SEARCH_SIZE_ELEMENT);
                if (endElementIndex > elementIndex) {
                    elementInfo = html.substring(
                            elementIndex + SEARCH_SIZE_ELEMENT, endElementIndex);
                }
            }
            String name="";
            String img=null;
            Element ele=null;
            elementIndex = endElementIndex;
            boolean build = true;
            String info = html.substring(index + SEARCH_SIZE, endIndex);
            if (elementInfo != null) {
                Matcher m = IMG_PATTERN.matcher(elementInfo);
                if (m.find()) {
                    String elementURL = m.group(1);
                    if (ICON_URLS.containsKey(elementURL)) {
                        Element e = ICON_URLS.get(elementURL);
                        ele=e;
                    } else {
                        build = false;
                    }
                } else {
                    build = false;
                }
            }
            Matcher m = IMG_PATTERN.matcher(info);
            if (m.find()) {
                img=m.group(1);
                int tmpStart=img.lastIndexOf("/")+1;
                int tmpEnd=img.lastIndexOf(".");
                int dataId=Integer.parseInt(img.substring(tmpStart,tmpEnd));
                id=dataId;
            } else {
                build = false;
            }
            m = NAME_PATTERN.matcher(info);
            if (m.find()) {
                String tmpName = m.group(1).replace("\n", "");

                while (tmpName.contains("  ")) {
                    tmpName = tmpName.replace("  ", " ");
                }
                name=tmpName.trim();
            } else {
                build = false;
            }
            if(build) {
                Ninja nin=new Ninja(name, img==null?null:new URL(URL_BASE+img), id, ele);
                ninjas.add(nin);
            }
            index = endIndex;
        }
        return ninjas;
    }
}
