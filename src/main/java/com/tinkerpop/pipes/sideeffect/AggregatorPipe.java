package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeClosure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * The AggregatorPipe produces a side effect that is the provided Collection filled with the contents of all the objects that have passed through it.
 * Before the first object is emitted from the AggregatorPipe, all of its incoming objects have been aggregated into the collection.
 * Finally, note that different Collections have different behaviors and write/read times.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AggregatorPipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S, Collection> {

    private Collection aggregate;
    private Queue<S> currentObjectQueue = new LinkedList<S>();
    private Queue<List> currentPathQueue = new LinkedList<List>();
    private List currentPath;
    private PipeClosure closure = null;

    public AggregatorPipe(final Collection aggregate) {
        this.aggregate = aggregate;
    }

    /**
     * The provided PipeClosure will process the object prior to inserting it into the aggregate collection.
     *
     * @param aggregate the aggregate collection
     * @param closure   a closure to process an object with prior to insertion into the collection
     */
    public AggregatorPipe(final Collection aggregate, final PipeClosure closure) {
        this(aggregate);
        this.closure = closure;
    }

    protected List getPathToHere() {
        return this.currentPath;
    }

    public List getPath() {
        final List pathElements = new ArrayList(getPathToHere());
        final int size = pathElements.size();
        // do not repeat filters as they dup the object
        if (size == 0 || pathElements.get(size - 1) != this.currentEnd) {
            pathElements.add(this.currentEnd);
        }
        return pathElements;
    }

    protected S processNextStart() {
        while (true) {
            if (this.currentObjectQueue.isEmpty()) {
                if (!this.starts.hasNext())
                    throw new NoSuchElementException();
                else {
                    this.currentObjectQueue.clear();
                    this.currentPathQueue.clear();
                    while (this.starts.hasNext()) {
                        final S s = this.starts.next();
                        if (this.closure != null)
                            this.aggregate.add(this.closure.compute(s));
                        else
                            this.aggregate.add(s);
                        this.currentObjectQueue.add(s);
                        this.currentPathQueue.add(super.getPathToHere());
                    }
                }
            } else {
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
