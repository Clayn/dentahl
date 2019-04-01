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
package net.bplaced.clayn.d4j.domain;

import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
import net.bplaced.clayn.d4j.domain.combo.Hit;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class Ninja implements Serializable
{

    private String name;
    private URL image;
    private int id;
    private transient Element elementType;
    private int element;
    private int main = 0;

    public Ninja()
    {
    }

    public Hit getOpak()
    {
        return null;
    }

    public Hit getAttack()
    {
        return null;
    }

    public void setMain(int main)
    {
        this.main = main;
    }

    public boolean isMain()
    {
        return main > 0;
    }

    public int getMain()
    {
        return main;
    }

    public Ninja(String name, URL image, int id, Element element)
    {
        if (id < 0)
        {
            throw new IllegalArgumentException();
        }
        this.name = Objects.requireNonNull(name);
        this.image = image;
        this.id = id;
        this.element = element == null ? -1 : element.ordinal();
        this.elementType = element;
    }

    public String getName()
    {
        return name;
    }

    public URL getImage()
    {
        return image;
    }

    public int getId()
    {
        return id;
    }

    public int getElement()
    {
        return element;
    }

    public Element getElementType()
    {
        return elementType == null ? element < 0 || element >= Element.values().length ? null : Element.values()[element] : elementType;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setImage(URL image)
    {
        this.image = image;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setElementType(Element elementType)
    {
        this.element = elementType == null ? -1 : elementType.ordinal();
        this.elementType = elementType;
    }

    public void setElement(int element)
    {
        this.element = element;
        this.elementType = element < 0 || element >= Element.values().length ? null : Element.values()[element];
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Ninja other = (Ninja) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Ninja{" + "name=" + name + ", image=" + image + ", id=" + id + ", element=" + getElementType() + ", main=" + isMain() + '}';
    }
}
