package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.FastNoSuchElementException;
import com.tinkerpop.pipes.util.structures.ArrayQueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * The AggregatePipe produces a side effect that is the provided Collection filled with the contents of all the objects that have passed through it.
 * Before the first object is emitted from the AggregatePipe, all of its incoming objects have been aggregated into the collection.
 * Finally, note that different Collections have different behaviors and write/read times.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AggregatePipe<S> extends AbstractPipe<S, S> implements SideEffectPipe.GreedySideEffectPipe<S, Collection> {

    private Collection aggregate;
    private Queue<S> currentObjectQueue = new ArrayQueue<S>();
    private Queue<List> currentPathQueue = new ArrayQueue<List>();
    private List currentPath;
    private PipeFunction<S, ?> preAggregateFunction = null;

    public AggregatePipe(final Collection aggregate) {
        this.aggregate = aggregate;
    }

    /**
     * The provided PipeFunction will process the object prior to inserting it into the aggregate collection.
     *
     * @param aggregate            the aggregate collection
     * @param preAggregateFunction a function to process an object with prior to insertion into the collection
     */
    public AggregatePipe(final Collection aggregate, final PipeFunction<S, ?> preAggregateFunction) {
        this(aggregate);
        this.preAggregateFunction = preAggregateFunction;
    }

    public List getCurrentPath() {
        if (this.pathEnabled) {
            final List pathElements = new ArrayList(this.currentPath);
            final int size = pathElements.size();
            // do not repeat filters as they dup the object
            if (size == 0 || pathElements.get(size - 1) != this.currentEnd) {
                pathElements.add(this.currentEnd);
            }
            return pathElements;
        } else {
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
        }
    }

    protected S processNextStart() {
        while (true) {
            if (this.currentObjectQueue.isEmpty()) {
                if (!this.starts.hasNext())
                    throw FastNoSuchElementException.instance();
                else {
                    this.currentObjectQueue.clear();
                    this.currentPathQueue.clear();

                    try {
                        while (true) {
                            final S s = this.starts.next();
                            if (this.preAggregateFunction != null)
                                this.aggregate.add(this.preAggregateFunction.compute(s));
                            else
                                this.aggregate.add(s);
                            this.currentObjectQueue.add(s);
                            if (this.pathEnabled)
                                this.currentPathQueue.add(this.getPathToHere());
                        }
                    } catch (final NoSuchElementException e) {
                    }
                }
            } else {
                if (this.pathEnabled)
                    this.currentPath = currentPathQueue.remove();
                return this.currentObjectQueue.remove();
            }
        }
    }

    public Collection getSideEffect() {
        return this.aggregate;
    }

    public void reset() {
        try {
            this.aggregate = this.aggregate.getClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        this.currentPath = null;
        this.currentObjectQueue.clear();
        this.currentPathQueue.clear();
        super.reset();
    }
}
