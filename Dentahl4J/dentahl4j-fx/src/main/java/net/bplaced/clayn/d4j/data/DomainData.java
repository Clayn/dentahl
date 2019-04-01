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
package net.bplaced.clayn.d4j.data;

import java.util.Comparator;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.SortedList;
import javafx.scene.image.Image;
import net.bplaced.clayn.d4j.domain.Ninja;
import net.bplaced.clayn.d4j.fx.custom.FXTeam;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class DomainData
{

    private static final DomainData INSTANCE = new DomainData();
    private final ObservableList<Ninja> ninjas = FXCollections.observableArrayList();
    private final SortedList<Ninja> sortedNinjas = new SortedList<>(ninjas);
    private final ObservableMap<Ninja, Image> ninjaImages = FXCollections.observableHashMap();
    private final ReadOnlyIntegerWrapper minimumId = new ReadOnlyIntegerWrapper(
            -1);
    private final ReadOnlyIntegerWrapper maxId = new ReadOnlyIntegerWrapper(-1);
    private final ObservableList<FXTeam> teams = FXCollections.observableArrayList();

    public int getMaxId()
    {
        return maxId.get();
    }

    public ReadOnlyIntegerProperty maxIdProperty()
    {
        return maxId.getReadOnlyProperty();
    }

    public int getMinimumId()
    {
        return minimumId.get();
    }

    public ReadOnlyIntegerProperty minimumIdProperty()
    {
        return minimumId.getReadOnlyProperty();
    }

    private DomainData()
    {
        sortedNinjas.setComparator(
                Comparator.comparingInt(Ninja::getMain).reversed().thenComparingInt(
                        Ninja::getId));
        minimumId.bind(Bindings.createIntegerBinding(
                () -> ninjas.stream().mapToInt(Ninja::getId).min().orElse(-1),
                ninjas));
        maxId.bind(Bindings.createIntegerBinding(
                () -> ninjas.stream().mapToInt(Ninja::getId).max().orElse(-1),
                ninjas));
    }

    public static DomainData getInstance()
    {
        return INSTANCE;
    }

    public ObservableList<Ninja> getNinjas()
    {
        return ninjas;
    }

    public SortedList<Ninja> getSortedNinjas()
    {
        return sortedNinjas;
    }

    public ObservableMap<Ninja, Image> getNinjaImages()
    {
        return ninjaImages;
    }

    public Ninja getNinja(long id)
    {
        if (id < minimumId.get() || id > maxId.get())
        {
            return null;
        }
        return ninjas.stream().filter((nin) -> nin.getId() == id).findFirst().orElse(
                null);
    }

    public ObservableList<FXTeam> getTeams()
    {
        return teams;
    }

}
