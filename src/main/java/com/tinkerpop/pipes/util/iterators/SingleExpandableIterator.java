package com.tinkerpop.pipes.util.iterators;

import com.tinkerpop.pipes.util.FastNoSuchElementException;

import java.util.Iterator;

/**
 * SingleExpandableIterator can have an object added to it. However, it only stores one object.
 * If the previous object hasn't be next()'d before adding a new one, the previous object is overwritten.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SingleExpandableIterator<T> implements Iterator<T> {

    public T t;
    private boolean alive;

    public SingleExpandableIterator(final T t) {
        this.t = t;
        this.alive = true;
    }

    public SingleExpandableIterator() {
        this.alive = false;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return this.alive;
    }

    public void add(final T t) {
        this.t = t;
        this.alive = true;
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