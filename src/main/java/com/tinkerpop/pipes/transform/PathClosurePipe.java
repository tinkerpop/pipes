package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * PathClosurePipe is analogous to PathPipe, except that for each path emitted, a closure is applied to the objects of that path.
 * The path closures are applied in a round robin fashion. As such, the number of closures need not equal the number of objects in the path.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathClosurePipe<S> extends AbstractPipe<S, List> {

    private final PipeClosure[] pathClosures;

    public PathClosurePipe(final PipeClosure... pathClosures) {
        this.pathClosures = pathClosures;
        for (PipeClosure pipeClosure : this.pathClosures) {
            pipeClosure.setPipe(this);
        }
    }

    public void setStarts(final Iterator<S> starts) {
        this.starts = starts;
    }

    public List processNextStart() {
        if (this.starts instanceof Pipe) {
            this.starts.next();
            final List path = ((Pipe) this.starts).getPath();
            final List closedPath = new LinkedList();
            int nextClosure = 0;
            for (Object object : path) {
                closedPath.add(pathClosures[nextClosure].compute(object));
                nextClosure = (nextClosure + 1) % pathClosures.length;
            }
            return closedPath;
        } else {
            throw new NoSuchElementException("The start of this pipe was not a pipe");
        }
    }


}
