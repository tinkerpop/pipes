package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.AbstractPipe;

import java.util.Arrays;
import java.util.Iterator;

/**
 * StartPipe is a handy was to create a pipe out of the provided object.
 * The provided object is set as the start of the Pipe that simply returns the object or
 * if the object is an iterator/iterable, the objects of the object.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class StartPipe<S> extends AbstractPipe<S, S> {

    private final Iterator starts;

    public StartPipe(final Object start) {
        if (start instanceof Iterator) {
            this.starts = (Iterator) start;
        } else if (start instanceof Iterable) {
            this.starts = ((Iterable) start).iterator();
        } else {
            this.starts = Arrays.asList(start).iterator();
        }
    }

    public S processNextStart() {
        return (S) this.starts.next();
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.starts);
    }
}
