package net.bplaced.clayn.d4j.domain.combo;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class Hit
{

    private final State state;
    private final int hits;

    public Hit(State state, int hits)
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

}
