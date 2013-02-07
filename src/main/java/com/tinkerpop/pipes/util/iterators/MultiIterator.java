package com.tinkerpop.pipes.util.iterators;

import com.tinkerpop.pipes.util.FastNoSuchElementException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * MultiIterator takes multiple iterators in its constructor and makes them behave like a single iterator.
 * The order in which objects are next()'d are with respect to the order of the iterators passed into the constructor.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class MultiIterator<T> implements Iterator<T> {

    private final Iterator<Iterator<T>> iterators;
    private Iterator<T> currentIterator = null;

    public MultiIterator(final Iterator<T>... iterators) {
        this(Arrays.asList(iterators));
    }

    public MultiIterator(final List<Iterator<T>> iterators) {
        this.iterators = iterators.iterator();
        if (this.iterators.hasNext())
            this.currentIterator = this.iterators.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public T next() {
        while (true) {
            if (this.currentIterator.hasNext()) {
                return this.currentIterator.next();
            } else {
                if (this.iterators.hasNext()) {
                    this.currentIterator = this.iterators.next();
                } else {
                    throw FastNoSuchElementException.instance();
                }
            }
        }
    }

    public boolean hasNext() {
        while (true) {
            if (null != this.currentIterator && this.currentIterator.hasNext()) {
                return true;
            } else if (this.iterators.hasNext()) {
                this.currentIterator = iterators.next();
            } else {
                return false;
            }
        }
    }
}