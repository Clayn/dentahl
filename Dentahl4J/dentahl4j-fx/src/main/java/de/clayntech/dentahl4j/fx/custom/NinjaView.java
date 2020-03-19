/*
 * The MIT License
 *
 * Copyright 2019 Your Organisation.
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
package de.clayntech.dentahl4j.fx.custom;

import de.clayntech.dentahl4j.domain.Ninja;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class NinjaView extends Control
{

    private final IntegerProperty xPosition = new SimpleIntegerProperty(-1);
    private final IntegerProperty yPosition = new SimpleIntegerProperty(-1);
    private final ReadOnlyBooleanWrapper dragable = new ReadOnlyBooleanWrapper(
            true);
    private final ReadOnlyBooleanWrapper dragableDetail = new ReadOnlyBooleanWrapper(
            true);

    public boolean isDragable()
    {
        return dragable.get();
    }

    public boolean isDragableDetail()
    {
        return dragableDetail.get();
    }

    ReadOnlyBooleanWrapper dragableAccessProperty()
    {
        return dragable;
    }

    public ReadOnlyBooleanProperty dragableProperty()
    {
        return dragable.getReadOnlyProperty();
    }

    ReadOnlyBooleanWrapper dragableDetailProperty()
    {
        return dragableDetail;
    }

    public int getYPosition()
    {
        return yPosition.get();
    }

    public void setYPosition(int value)
    {
        yPosition.set(value);
    }

    public IntegerProperty yPositionProperty()
    {
        return yPosition;
    }

    public int getXPosition()
    {
        return xPosition.get();
    }

    public void setXPosition(int value)
    {
        xPosition.set(value);
    }

    public IntegerProperty xPositionProperty()
    {
        return xPosition;
    }

    public boolean isTeamPosition()
    {
        return getXPosition() < 3 && getXPosition() >= 0 && getYPosition() < 3 && getYPosition() >= 0;
    }

    private final ObjectProperty<Ninja> ninja = new SimpleObjectProperty<>();

    public Ninja getNinja()
    {
        return ninja.get();
    }

    public void setNinja(Ninja value)
    {
        ninja.set(value);
    }

    public ObjectProperty ninjaProperty()
    {
        return ninja;
    }

    @Override
    protected Skin<?> createDefaultSkin()
    {
        return new NinjaViewSkin(this);
    }

}
