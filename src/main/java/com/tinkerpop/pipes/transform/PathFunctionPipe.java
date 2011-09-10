package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeFunction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * PathFunctionPipe is analogous to PathPipe, except that for each path emitted, a function is applied to the objects of that path.
 * The path functions are applied in a round robin fashion. As such, the number of functions need not equal the number of objects in the path.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathFunctionPipe<S> extends AbstractPipe<S, List> {

    private final PipeFunction[] pathFunctions;

    public PathFunctionPipe(final PipeFunction... pathFunctions) {
        this.pathFunctions = pathFunctions;
    }

    public void setStarts(final Iterator<S> starts) {
        this.starts = starts;
    }

    public List processNextStart() {
        if (this.starts instanceof Pipe) {
            this.starts.next();
            final List path = ((Pipe) this.starts).getPath();
            final List closedPath = new LinkedList();
            int nextFunction = 0;
            for (Object object : path) {
                closedPath.add(pathFunctions[nextFunction].compute(object));
                nextFunction = (nextFunction + 1) % pathFunctions.length;
            }
            return closedPath;
        } else {
            throw new NoSuchElementException("The start of this pipe was not a pipe");
        }
    }


}
