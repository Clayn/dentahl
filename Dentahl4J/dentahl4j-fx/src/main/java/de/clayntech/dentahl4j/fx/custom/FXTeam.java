package de.clayntech.dentahl4j.fx.custom;

import de.clayntech.dentahl4j.domain.Ninja;
import de.clayntech.dentahl4j.domain.Team;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.Objects;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class FXTeam extends Team
{

    private final ObservableMap<Integer, Ninja> positions = FXCollections.observableMap(
            super.getPositions());
    private final ReadOnlyIntegerWrapper ninjaCount = new ReadOnlyIntegerWrapper(
            -1);

    public FXTeam()
    {
        ninjaCount.bind(Bindings.createIntegerBinding(
                () -> (int) positions.values().stream().filter(Objects::nonNull).count(),
                positions));
    }

    public int getNinjaCount()
    {
        return ninjaCount.get();
    }

    public ReadOnlyIntegerProperty ninjaCountProperty()
    {
        return ninjaCount.getReadOnlyProperty();
    }

    @Override
    public ObservableMap<Integer, Ninja> getPositions()
    {
        return positions;
    }

    public static FXTeam fromDomainTeam(Team t)
    {
        FXTeam team = new FXTeam();
        team.setDescription(t.getDescription());
        team.setName(t.getName());
        team.setId(t.getId());
        team.getPositions().putAll(t.getPositions());
        return team;
    }

    public Team toDomainTeam() {
        Team t=new Team();
        t.setName(getName());
        t.setDescription(getDescription());
        t.setId(getId());
        t.getPositions().putAll(getPositions());
        return t;
    }
}
