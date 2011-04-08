package com.tinkerpop.pipes;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * EmptyIterator returns false for hasNext() and throws a NoSuchElementException for next().
 * In short, its an empty iterator.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class EmptyIterator<T> implements Iterator<T> {

    public T next() {
        throw new NoSuchElementException();
    }

    public boolean hasNext() {
        return false;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
