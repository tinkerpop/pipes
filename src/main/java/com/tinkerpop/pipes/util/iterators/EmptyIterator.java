package com.tinkerpop.pipes.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class EmptyIterator<T> implements Iterator<T> {

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return false;
    }

    public T next() {
        throw new NoSuchElementException();
    }
}
