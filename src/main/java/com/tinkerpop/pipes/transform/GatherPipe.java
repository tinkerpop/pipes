package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.FastNoSuchElementException;

import java.util.ArrayList;
import java.util.List;

/**
 * GatherPipe emits all the objects up to this step as an ArrayList.
 * This pipe is useful for doing breadth-first traversal where a List of all the current steps objects are gathered up.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GatherPipe<S> extends AbstractPipe<S, List<S>> implements TransformPipe<S, List<S>> {

    private List<List> listPaths = new ArrayList<List>();


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
            throw FastNoSuchElementException.instance();
        } else {
            while (this.starts.hasNext()) {
                final S s = this.starts.next();
                list.add(s);
                if (this.pathEnabled)
                    this.listPaths.add(super.getPathToHere());
            }
        }

        if (this.pathEnabled) {
            return addList(list);
        } else {
            return list;
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