package com.tinkerpop.pipes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExpandableIterator<T> implements Iterator<T> {

    private final Queue<T> queue = new LinkedList<T>();
    private final Iterator<T> iterator;

    public ExpandableIterator(final Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public T next() {
        if (this.queue.isEmpty()) {
            return iterator.next();
        } else {
            return this.queue.remove();
        }
    }

    public boolean hasNext() {
        return !this.queue.isEmpty() || this.iterator.hasNext();
    }

    public void add(final T t) {
        this.queue.add(t);
    }
}
