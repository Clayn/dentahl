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
package net.bplaced.clayn.d4j.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import net.bplaced.clayn.d4j.domain.Element;
import net.bplaced.clayn.d4j.domain.Ninja;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class DBHelper
{

    private static final String NINJA_SQL = "INSERT INTO `Ninja` (`id`, `name`, `image`, `element`) VALUES (NULL, '%s', %s, '%d');";

    private static final String ELEMENT_SQL = "INSERT INTO `Element` (`id`, `name`, `image`) VALUES (%d, '%s', '%s');";

    private static final Map<Element, String> ICON_URLS = new HashMap<>();

    static
    {
        String tmp = "<span class=\"attr\"><img src=\"/assets/images/icons/elements/de/feng_icon.png\" width=\"23\" height=\"24\"></span>";
        ICON_URLS.put(
                Element.FIRE, "/assets/images/icons/elements/de/huo_icon.png");
        ICON_URLS.put(
                Element.WIND, "/assets/images/icons/elements/de/feng_icon.png");
        ICON_URLS.put(
                Element.LIGHTNING,
                "/assets/images/icons/elements/de/lei_icon.png");
        ICON_URLS.put(
                Element.EARTH, "/assets/images/icons/elements/de/tu_icon.png");
        ICON_URLS.put(
                Element.WATER, "/assets/images/icons/elements/de/shui_icon.png");
    }

    public static String toInsertStatement(Ninja n, File output) throws Exception
    {
        if (!output.exists())
        {
            output.createNewFile();
        }
        try (BufferedWriter writer = Files.newBufferedWriter(output.toPath()))
        {
            String statement = String.format(NINJA_SQL, n.getName(),
                    n.getImage() == null ? "NULL" : "'" + n.getImage() + "'",
                    n.getElement());
            writer.write(statement);
            writer.flush();
            return statement;
        }
    }

    public static String toInsertStatement(Element el, File output) throws Exception
    {
        if (!output.exists())
        {
            output.createNewFile();
        }
        String tmp = el.name();
        String name = tmp.substring(0, 1) + tmp.substring(1).toLowerCase();

        try (BufferedWriter writer = Files.newBufferedWriter(output.toPath()))
        {
            String statement = String.format(ELEMENT_SQL, el.ordinal(), name,
                    ICON_URLS.get(el));
            writer.write(statement);
            writer.flush();
            return statement;
        }
    }
}
