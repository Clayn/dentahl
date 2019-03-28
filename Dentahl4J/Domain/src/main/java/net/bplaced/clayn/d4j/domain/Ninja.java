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

import java.net.URL;
import java.util.Objects;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class Ninja {
    private final String name;
    private final URL image;
    private final int id;
    private final Element element;

    public Ninja(String name, URL image, int id, Element element) {
        if(id<0) {
            throw new IllegalArgumentException();
        }
        this.name = Objects.requireNonNull(name);
        this.image = image;
        this.id = id;
        this.element = Objects.requireNonNull(element);
    }

    public String getName() {
        return name;
    }

    public URL getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public Element getElement() {
        return element;
    }

    @Override
    public String toString() {
        return "Ninja{" + "name=" + name + ", image=" + image + ", id=" + id + ", element=" + element + '}';
    }
}
