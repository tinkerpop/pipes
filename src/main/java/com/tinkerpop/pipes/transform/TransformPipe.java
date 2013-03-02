package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface TransformPipe<S, E> extends Pipe<S, E> {

    public enum Order {
        DECR, INCR
    }
}
