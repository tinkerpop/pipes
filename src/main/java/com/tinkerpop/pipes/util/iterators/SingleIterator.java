package com.tinkerpop.pipes.util.iterators;

import com.tinkerpop.pipes.util.FastNoSuchElementException;

import java.util.Iterator;

/**
 * SingleIterator is an iterator that only contains one object of type T.
 * This is more efficient than using Arrays.asList(object).iterator() (approx. 2x faster).
 * This has applications in various metapipes, where single objects are manipulated at a time.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SingleIterator<T> implements Iterator<T> {

    private final T t;
    private boolean alive = true;

    public SingleIterator(final T t) {
        this.t = t;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return this.alive;
    }

    public T next() {
        if (this.alive) {
            this.alive = false;
            return this.t;
        } else {
            throw FastNoSuchElementException.instance();
        }
    }
}
