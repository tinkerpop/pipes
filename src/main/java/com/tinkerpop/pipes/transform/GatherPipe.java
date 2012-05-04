package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * GatherPipe emits all the objects up to this step as an ArrayList.
 * This pipe is useful for doing breadth-first traversal where a List of all the current steps objects are gathered up.
 * This gathered up List can then be filtered by the provided postFilterFunction and thus, a selective branch breadth-first traversal can be enacted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherPipe<S> extends AbstractPipe<S, List<S>> {

    private List<List> listPaths = new ArrayList<List>();
    private final PipeFunction<List<S>, List<S>> postFilterFunction;

    public GatherPipe() {
        this(null);
    }

    public GatherPipe(final PipeFunction<List<S>, List<S>> postFilterFunction) {
        this.postFilterFunction = postFilterFunction;
    }

    public List getCurrentPath() {
        if (pathEnabled)
            return new ArrayList(this.listPaths);
        else
            throw new RuntimeException(Pipe.NO_PATH_MESSAGE);
    }

    protected List<S> processNextStart() {
        final List<S> list = new ArrayList<S>();
        this.listPaths = new ArrayList<List>();
        if (!this.starts.hasNext()) {
            throw new NoSuchElementException();
        } else {
            while (this.starts.hasNext()) {
                final S s = this.starts.next();
                list.add(s);
                if (this.pathEnabled)
                    this.listPaths.add(super.getPathToHere());
            }
        }

        if (this.pathEnabled) {
            if (null != this.postFilterFunction) {
                return addList(this.postFilterFunction.compute(list));
            } else {
                return addList(list);
            }
        } else {
            if (null != this.postFilterFunction) {
                return this.postFilterFunction.compute(list);
            } else {
                return list;
            }
        }
    }

    public void reset() {
        this.listPaths = new ArrayList<List>();
        super.reset();
    }

    private List addList(final List list) {
        for (final List l : this.listPaths) {
            l.add(list);
        }
        return list;
    }


}