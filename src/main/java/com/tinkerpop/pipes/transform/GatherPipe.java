package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * GatherPipe emits all the elements up to this step as a LinkedList.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherPipe<S> extends AbstractPipe<S, List<S>> {

    private List<List> collectionPaths = new LinkedList<List>();
    private PipeFunction<List<S>, List<S>> postFilterFunction = null;

    public GatherPipe() {

    }

    public GatherPipe(final PipeFunction<List<S>, List<S>> postFilterFunction) {
        this.postFilterFunction = postFilterFunction;
    }

    protected List getPathToHere() {
        return this.collectionPaths;
    }

    /*public List getPath() {
        final List pathElements = new ArrayList(getPathToHere());
        final int size = pathElements.size();
        // do not repeat filters as they dup the object
        if (size == 0 || pathElements.get(size - 1) != this.currentEnd) {
            pathElements.add(this.currentEnd);
        }
        return pathElements;
    }*/

    protected List<S> processNextStart() {
        final List<S> collection = new LinkedList<S>();
        this.collectionPaths = new LinkedList<List>();
        if (!this.starts.hasNext()) {
            throw new NoSuchElementException();
        } else {
            while (this.starts.hasNext()) {
                final S s = this.starts.next();
                collection.add(s);
                this.collectionPaths.add(super.getPathToHere());
            }
        }
        if (null != this.postFilterFunction) {
            return this.postFilterFunction.compute(collection);
        } else {
            return collection;
        }
    }


    public void reset() {
        this.collectionPaths = new LinkedList<List>();
        super.reset();
    }
}