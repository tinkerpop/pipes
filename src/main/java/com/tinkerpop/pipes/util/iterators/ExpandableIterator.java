package com.tinkerpop.pipes.util.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * ExpandableIterator takes an iterator and will emit the objects of that iterator.
 * However, if an object is added to ExpandableIterator, then its put into a queue.
 * The queue has priority over the iterator when calling next().
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExpandableIterator<T> implements Iterator<T> {

    private final Queue<T> queue = new LinkedList<T>();
    private final Iterator<T> iterator;

    public ExpandableIterator(final Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public ExpandableIterator() {
        this.iterator = (Iterator<T>) EmptyIterator.INSTANCE;
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
