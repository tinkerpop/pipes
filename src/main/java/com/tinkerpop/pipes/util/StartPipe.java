package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.IdentityPipe;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

import java.util.Iterator;

/**
 * StartPipe is a handy way to create a pipe out of the provided object.
 * The provided object is set as the start of the Pipe that simply emits the object or
 * if the object is an iterator/iterable, the objects of the object.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class StartPipe<S> extends IdentityPipe<S> {

    public StartPipe(final Object start) {
        if (start instanceof Iterator) {
            super.setStarts((Iterator) start);
        } else if (start instanceof Iterable) {
            super.setStarts((Iterable) start);
        } else {
            super.setStarts(new SingleIterator(start));
        }
    }
}
