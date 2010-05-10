package com.tinkerpop.pipes.serial.split;

import com.tinkerpop.pipes.serial.Pipe;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class SplitQueuePipe<S> implements Pipe<S, S> {

    private final Queue<S> queue = new LinkedList<S>();
    private final SplitPipe<S> splitPipe;

    public SplitQueuePipe(final SplitPipe<S> splitPipe) {
        this.splitPipe = splitPipe;
    }

    public void add(final S element) {
        this.queue.add(element);
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
        if (this.queue.isEmpty()) {
            while (this.splitPipe.hasNext()) {
                this.splitPipe.routeNext();
                if (!this.queue.isEmpty())
                    return;
            }
        }
    }

    public void setStarts(Iterator<S> starts) {
        throw new UnsupportedOperationException();
    }

    public void setStarts(Iterable<S> starts) {
        throw new UnsupportedOperationException();
    }

    public Iterator<S> iterator() {
        return this;
    }

}
