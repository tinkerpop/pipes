package com.tinkerpop.pipes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class MultiIterator<T> implements Iterator<T> {

    private final List<Iterator<T>> iterators;

    public MultiIterator(final Iterator<T>... iterators) {
        this.iterators = Arrays.asList(iterators);
    }

    public MultiIterator(final List<Iterator<T>> iterators) {
        this.iterators = iterators;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public T next() {
        for (Iterator<T> itty : this.iterators) {
            if (itty.hasNext())
                return itty.next();
        }
        throw new NoSuchElementException();
    }

    public boolean hasNext() {
        for (Iterator<T> itty : this.iterators) {
            if (itty.hasNext())
                return true;
        }
        return false;
    }


}