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

    private List<List> listPaths = new LinkedList<List>();
    private PipeFunction<List<S>, List<S>> postFilterFunction = null;

    public GatherPipe() {

    }

    public GatherPipe(final PipeFunction<List<S>, List<S>> postFilterFunction) {
        this.postFilterFunction = postFilterFunction;
    }

    public List getPath() {
        return this.listPaths;
    }

    protected List<S> processNextStart() {
        final List<S> list = new LinkedList<S>();
        this.listPaths = new LinkedList<List>();
        if (!this.starts.hasNext()) {
            throw new NoSuchElementException();
        } else {
            while (this.starts.hasNext()) {
                final S s = this.starts.next();
                list.add(s);
                this.listPaths.add(this.getPathToHere());
            }
        }
        if (null != this.postFilterFunction) {
            return this.postFilterFunction.compute(list);
        } else {
            return list;
        }
    }


    public void reset() {
        this.listPaths = new LinkedList<List>();
        super.reset();
    }
}