package com.tinkerpop.pipes.serial;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SingleIterator<T> implements Iterator<T> {

    private final T t;
    private boolean alive = true;

    public SingleIterator(T t) {
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
            return t;
        } else {
            throw new NoSuchElementException();
        }
    }
}
