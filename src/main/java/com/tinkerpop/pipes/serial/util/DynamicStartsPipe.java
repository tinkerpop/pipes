package com.tinkerpop.pipes.serial.util;

import com.tinkerpop.pipes.serial.AbstractPipe;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class DynamicStartsPipe<S> extends AbstractPipe<S, S> {

    private final Queue<S> queue = new LinkedList<S>();

    public void addStart(final S start) {
        this.queue.add(start);
    }


    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        if (this.queue.isEmpty()) {
            this.prepareNext();
            return !this.queue.isEmpty();
        } else {
            return true;
        }
    }

    public S next() {
        if (this.queue.isEmpty()) {
            this.prepareNext();
        }
        if (this.queue.isEmpty())
            throw new NoSuchElementException();
        else {
            S temp = queue.remove();
            this.prepareNext();
            return temp;
        }
    }

    private void prepareNext() {
        try {
            this.queue.add(this.starts.next());
        } catch (NoSuchElementException e) {
        }
    }
}