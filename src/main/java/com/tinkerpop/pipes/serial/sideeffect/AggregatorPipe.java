package com.tinkerpop.pipes.serial.sideeffect;


import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.Collection;
import java.util.Iterator;

/**
 * The AggregatorPipe produces a side effect that is the provided Collection filled with the contents of all the objects that have passed through it.
 * Before the first object is emitted from the AggregatorPipe, all of its incoming objects have been aggregated into the collection.
 * The collection iterator is used as the emitting iterator. Thus, what goes into AggregatorPipe may not be the same as what comes out of AggregatorPipe.
 * For example, duplicates removed, different order to the stream, etc.
 * Finally, note that different Collections have different behaviors and write/read times.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AggregatorPipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S, Collection<S>> {

    private final Collection<S> aggregate;
    private Iterator<S> aggregateIterator = null;

    public AggregatorPipe(final Collection<S> collection) {
        this.aggregate = collection;
    }

    protected S processNextStart() {
        if (null == this.aggregateIterator) {
            while (this.starts.hasNext()) {
                aggregate.add(this.starts.next());
            }
            aggregateIterator = aggregate.iterator();
        }
        return this.aggregateIterator.next();
    }

    public Collection<S> getSideEffect() {
        return this.aggregate;
    }
}
