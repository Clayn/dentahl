package net.bplaced.clayn.d4j.domain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class Team
{

    private String name;
    private String description;
    private int id;
    private final Map<Integer, Ninja> positions = new HashMap<>();

    public Map<Integer, Ninja> getPositions()
    {
        return positions;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return "Team{" + "name=" + name + ", description=" + description + ", id=" + id + ", positions=" + positions + '}';
    }

}
