package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * PathPipe emits the path that the traverser has taken up to this object.
 * If provided, the path functions are applied in a round robin fashion.
 * As such, the number of functions need not equal the number of objects in the path.
 * This pipe requires that path calculations be enabled. As such, when the start is set, enablePath(true) is invoked.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathPipe<S> extends AbstractPipe<S, List> implements TransformPipe<S, List> {

    private final PipeFunction[] pathFunctions;

    public PathPipe(final PipeFunction... pathFunctions) {
        if (pathFunctions.length == 0) {
            this.pathFunctions = null;
        } else {
            this.pathFunctions = pathFunctions;
        }
    }

    public void setStarts(final Iterator<S> starts) {
        super.setStarts(starts);
        this.enablePath(true);
    }

    public List processNextStart() {
        if (this.starts instanceof Pipe) {
            this.starts.next();
            final List path = ((Pipe) this.starts).getCurrentPath();
            if (null == this.pathFunctions) {
                return path;
            } else {
                final List closedPath = new ArrayList();
                int nextFunction = 0;
                for (final Object object : path) {
                    closedPath.add(this.pathFunctions[nextFunction].compute(object));
                    nextFunction = (nextFunction + 1) % this.pathFunctions.length;
                }
                return closedPath;
            }
        } else {
            throw new NoSuchElementException("The start of this pipe was not a pipe");
        }
    }


}
