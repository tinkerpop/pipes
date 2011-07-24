package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Emits the path that the traverser has taken up to this object.
 * In other words, it uses getPath() of the previous pipe to emit the transformation stages.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathPipe<S> extends AbstractPipe<S, List> {

    public void setStarts(Iterator<S> starts) {
        this.starts = starts;
    }

    public List processNextStart() {
        if (this.starts instanceof Pipe) {
            this.starts.next();
            return ((Pipe) this.starts).getPath();
        } else {
            throw new NoSuchElementException("The start of this pipe was not a pipe");
        }
    }
}
