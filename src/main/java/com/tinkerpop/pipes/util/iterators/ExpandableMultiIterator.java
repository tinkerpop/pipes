package com.tinkerpop.pipes.util.iterators;

import com.tinkerpop.pipes.util.FastNoSuchElementException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ExpandableMultiIterator<T> implements Iterator<T> {

    private final ExpandableIterator<Iterator<T>> iterators = new ExpandableIterator<Iterator<T>>();
    private Iterator<T> currentIterator = null;

    public ExpandableMultiIterator(final Iterator<T>... iterators) {
        this(Arrays.asList(iterators));
    }

    public ExpandableMultiIterator(final List<Iterator<T>> iterators) {
        for (Iterator<T> itty : iterators) {
            this.iterators.add(itty);
        }
    }

    public ExpandableMultiIterator() {

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void addIterator(final Iterator<T> itty) {
        this.iterators.add(itty);
    }

    public T next() {
        while (true) {
            if (null != this.currentIterator && this.currentIterator.hasNext()) {
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