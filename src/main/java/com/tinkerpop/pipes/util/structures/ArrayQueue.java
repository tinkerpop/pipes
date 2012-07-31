package com.tinkerpop.pipes.util.structures;

import java.util.ArrayList;
import java.util.Queue;

import com.tinkerpop.pipes.util.FastNoSuchElementException;

/**
 * A Queue implementation that does not remove items when "drained," but instead, simply makes use of a counter.
 *
 * @author Matthias Broecheler
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ArrayQueue<T> extends ArrayList<T> implements Queue<T> {

    private int current = 0;

    public T remove() {
        if (this.current >= this.size())
            throw FastNoSuchElementException.instance();
        else
            return this.get(this.current++);
    }

    public T peek() {
        if (this.current >= this.size())
            return null;
        else
            return this.get(this.current);
    }

    public boolean isEmpty() {
        return this.current >= this.size();
    }

    public void clear() {
        super.clear();
        this.current = 0;
    }

    public T poll() {
        if (this.current >= this.size())
            return null;
        else
            return remove();
    }

    public T element() {
        if (this.current >= this.size())
            throw FastNoSuchElementException.instance();
        else
            return peek();
    }

    public boolean offer(final T t) {
        return this.add(t);
    }
}
