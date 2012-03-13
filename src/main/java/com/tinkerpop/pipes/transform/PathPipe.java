package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Emits the path that the traverser has taken up to this object.
 * In other words, it uses getCurrentPath() of the previous pipe to emit the transformation stages.
 * This pipe requires that path calculations be enabled. As such, when the start is set, enablePath(true) is invoked.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathPipe<S> extends AbstractPipe<S, List> {

    public void setStarts(Iterator<S> starts) {
        this.starts = starts;
        this.enablePath(true);
    }

    public List processNextStart() {
        if (this.starts instanceof Pipe) {
            this.starts.next();
            return ((Pipe) this.starts).getCurrentPath();
        } else {
            throw new NoSuchElementException("The start of this pipe must be a pipe");
        }
    }
}
