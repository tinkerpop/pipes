package com.tinkerpop.pipes.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public enum EmptyIterator implements Iterator<Object> {
    INSTANCE;

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return false;
    }

    public Object next() {
        throw new NoSuchElementException();
    }
}