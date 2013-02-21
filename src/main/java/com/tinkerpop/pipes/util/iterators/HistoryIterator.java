package com.tinkerpop.pipes.util.iterators;

import java.util.Iterator;

/**
 * HistoryIterator wraps an iterator. It will behave like the wrapped iterator.
 * However, it will remember what was last next()'ed object out of the iterator via getLast().
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class HistoryIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private T last;

    public HistoryIterator(final Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    public T next() {
        this.last = this.iterator.next();
        return last;
    }

    public T getLast() {
        return this.last;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
