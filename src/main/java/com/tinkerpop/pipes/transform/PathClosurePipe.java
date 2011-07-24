package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathClosurePipe<S> extends AbstractPipe<S, List> {

    private final PipeClosure[] closures;

    public PathClosurePipe(PipeClosure... closures) {
        this.closures = closures;
    }

    public void setStarts(Iterator<S> starts) {
        this.starts = starts;
    }

    public List processNextStart() {
        if (this.starts instanceof Pipe) {
            this.starts.next();
            final List path = ((Pipe) this.starts).getPath();
            final List closedPath = new LinkedList();
            int nextClosure = 0;
            for (Object object : path) {
                closedPath.add(closures[nextClosure].compute(object));
                nextClosure = (nextClosure + 1) % closures.length;
            }
            return closedPath;
        } else {
            throw new NoSuchElementException("The start of this pipe was not a pipe");
        }
    }


}
