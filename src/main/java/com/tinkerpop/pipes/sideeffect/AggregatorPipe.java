package com.tinkerpop.pipes.sideeffect;


import com.tinkerpop.pipes.AbstractPipe;

import java.util.Collection;
import java.util.LinkedList;

/**
 * The AggregatorPipe produces a side effect that is the provided Collection filled with the contents of all the objects that have passed through it.
 * The incoming objects are emitted as is.
 * Note that different Collections have different behaviors and write/read times.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AggregatorPipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S, Collection<S>> {

    private final Collection<S> aggregate;

    public AggregatorPipe() {
        this.aggregate = new LinkedList<S>();
    }

    public AggregatorPipe(final Collection<S> collection) {
        this.aggregate = collection;
    }

    protected S processNextStart() {
        final S start = this.starts.next();
        this.aggregate.add(start);
        return start;
    }

    public Collection<S> getSideEffect() {
        return this.aggregate;
    }
}
