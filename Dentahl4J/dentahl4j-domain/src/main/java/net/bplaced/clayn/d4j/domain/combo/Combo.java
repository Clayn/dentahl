package net.bplaced.clayn.d4j.domain.combo;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class Combo
{

    private State state = null;
    private int hits = 0;

    private Combo(State state, int hits)
    {
        this.state = state;
        this.hits = hits;
    }

    public State getState()
    {
        return state;
    }

    public int getHits()
    {
        return hits;
    }

    public static Combo start(State state, int hits)
    {
        return new Combo(state, hits);
    }

    public static Combo start(State state)
    {
        return start(state, 1);
    }

    public Combo followedBy(State state, int hits)
    {
        this.state = state;
        this.hits += hits;
        return this;
    }

    public Combo followedBy(State state)
    {
        return followedBy(state, 1);
    }
}
