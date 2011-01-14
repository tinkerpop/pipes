package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.AbstractPipe;

/**
 * A TypeCastPipe will take an object of type S and typecast it to an object of type E.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TypeCastPipe<S, E> extends AbstractPipe<S, E> {

    public E processNextStart() {
        return (E) this.starts.next();
    }
}
