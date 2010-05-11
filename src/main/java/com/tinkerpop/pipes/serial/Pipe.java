package com.tinkerpop.pipes.serial;

import java.util.Iterator;

/**
 * The generic interface for any Pipe implementation.
 * A Pipe takes/consumes objects of type S and returns/emits objects of type E.
 * S refers to <i>starts</i> and the E refers to <i>ends</i>.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface Pipe<S, E> extends Iterator<E>, Iterable<E> {

    public void setStarts(Iterator<S> starts);

    public void setStarts(Iterable<S> starts);
}
