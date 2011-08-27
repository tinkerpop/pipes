package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.transform.IdentityPipe;

import java.util.Iterator;

/**
 * StartPipe is a handy was to create a pipe out of the provided object.
 * The provided object is set as the start of the Pipe that simply returns the object or
 * if the object is an iterator/iterable, the objects of the object.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class StartPipe<S> extends IdentityPipe<S> {

    public StartPipe(final Object start) {
        if (start instanceof Iterator) {
            this.setStarts((Iterator) start);
        } else if (start instanceof Iterable) {
            this.setStarts((Iterable) start);
        } else {
            this.setStarts(new SingleIterator(start));
        }
    }
}
