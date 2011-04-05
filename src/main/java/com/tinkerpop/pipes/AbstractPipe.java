package com.tinkerpop.pipes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An AbstractPipe provides most of the functionality that is repeated in every instance of a Pipe.
 * Any subclass of AbstractPipe should simply implement processNextStart(). The standard model is
 * <pre>
 * protected E processNextStart() throws NoSuchElementException {
 * S s = this.starts.next();
 * E e = // do something with the S to yield an E
 * return e;
 * }
 * </pre>
 * If the current incoming S is not to be emitted and there are no other S objects to process and emit, then throw a NoSuchElementException.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractPipe<S, E> implements Pipe<S, E> {

    protected Iterator<S> starts;
    private E nextEnd;
    protected E currentEnd;
    private boolean available = false;

    public void setStarts(final Pipe<?, S> starts) {
        this.starts = starts;
    }

    public void setStarts(final Iterator<S> starts) {
        if (starts instanceof Pipe)
            this.starts = starts;
        else
            this.starts = new HistoryIterator<S>(starts);
    }

    public void setStarts(final Iterable<S> starts) {
        this.setStarts(starts.iterator());
    }

    public void reset() {
        if (this.starts instanceof Pipe) {
            Pipe pipe = (Pipe) this.starts;
            pipe.reset();
        }
        this.nextEnd = null;
        this.currentEnd = null;
        this.available = false;
    }

    public List getPath() {
        final List pathElements = getPathToHere();
        final int size = pathElements.size();
        // do not repeat filters as they dup the object
        if (size == 0 || pathElements.get(size - 1) != this.currentEnd) {
            pathElements.add(this.currentEnd);
        }
        return pathElements;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public E next() {
        if (this.available) {
            this.available = false;
            this.currentEnd = this.nextEnd;
            return this.currentEnd;
        } else {
            this.currentEnd = this.processNextStart();
            return this.currentEnd;
        }
    }

    public boolean hasNext() {
        if (this.available)
            return true;
        else {
            try {
                this.nextEnd = this.processNextStart();
                this.available = true;
                return true;
            } catch (NoSuchElementException e) {
                this.available = false;
                return false;
            }
        }
    }

    public Iterator<E> iterator() {
        return this;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    protected abstract E processNextStart() throws NoSuchElementException;

    protected List getPathToHere() {
        if (this.starts instanceof Pipe) {
            return ((Pipe) this.starts).getPath();
        } else if (this.starts instanceof HistoryIterator) {
            final List list = new ArrayList();
            list.add(((HistoryIterator) starts).getLast());
            return list;
        } else {
            return new ArrayList();
        }
    }


}

