package de.clayntech.dentahl4j.fx.custom;

import java.util.Optional;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class TeamView extends Control
{

    private final ObjectProperty<Runnable> onClear = new SimpleObjectProperty<>();
    private final ObjectProperty<FXTeam> team = new SimpleObjectProperty<>(null);

    public FXTeam getTeam()
    {
        return team.get();
    }

    public void setTeam(FXTeam value)
    {
        team.set(value);
    }

    public ObjectProperty<FXTeam> teamProperty()
    {
        return team;
    }

    Runnable getOnClear()
    {
        return onClear.get();
    }

    void setOnClear(Runnable value)
    {
        onClear.set(value);
    }

    ObjectProperty onClearProperty()
    {
        return onClear;
    }

    @Override
    protected Skin<?> createDefaultSkin()
    {
        return new TeamViewSkin(this);
    }

    public void clear()
    {
        Optional.ofNullable(onClear.get()).ifPresent(Runnable::run);
    }
}
