package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;

/**
 * A TransformPipe will take an object of one type and transform it to an object of another type.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface TransformPipe<S, E> extends Pipe<S, E> {

    public enum Order {
        DECR, INCR
    }
}
