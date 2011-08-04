package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;

/**
 * A FilterPipe has no specified behavior save that it takes the same objects it emits.
 * This interface is used to specify that a Pipe will either emit its input or not.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface FilterPipe<S> extends Pipe<S, S> {
    enum Filter {
        EQUAL, NOT_EQUAL, GREATER_THAN, LESS_THAN, GREATER_THAN_EQUAL, LESS_THAN_EQUAL
    }
}
