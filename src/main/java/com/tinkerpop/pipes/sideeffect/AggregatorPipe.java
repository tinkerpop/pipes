package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;

import java.util.*;

/**
 * The AggregatorPipe produces a side effect that is the provided Collection filled with the contents of all the objects that have passed through it.
 * Before the first object is emitted from the AggregatorPipe, all of its incoming objects have been aggregated into the collection.
 * Finally, note that different Collections have different behaviors and write/read times.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AggregatorPipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S, Collection<S>> {

    private Collection<S> aggregate;
    private Queue<S> currentObjectQueue = new LinkedList<S>();
    private Queue<List> currentPathQueue = new LinkedList<List>();
    private List currentPath;

    public AggregatorPipe(final Collection<S> collection) {
        this.aggregate = collection;
    }

    protected List getPathToHere() {
        return currentPath;
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

    public Collection<S> getSideEffect() {
        return this.aggregate;
    }

    public void reset() {
        try {
            this.aggregate = (Collection<S>) this.aggregate.getClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        this.currentPath = null;
        this.currentObjectQueue.clear();
        this.currentPathQueue.clear();
        super.reset();
    }
}
