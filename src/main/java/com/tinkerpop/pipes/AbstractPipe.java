package com.tinkerpop.pipes;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

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
    private E currentEnd;
    private boolean available = false;
    protected boolean ensurePipeStarts = true;
    protected boolean pathEnabled = false;

    public void setStarts(final Pipe<?, S> starts) {
        this.starts = starts;
    }

    public void setStarts(final Iterator<S> starts) {
        if (ensurePipeStarts) {
            IdentityPipe<S> pipe = new IdentityPipe<S>();
            pipe.setStarts(starts);
            this.starts = pipe;
        } else {
            this.starts = starts;
        }
    }

    public void setStarts(final Iterable<S> starts) {
        this.setStarts(starts.iterator());
    }

    public void enablePath() {
        this.pathEnabled = true;
        if (this.starts instanceof Path) {
            Path path = (Path)this.starts;
            path.enablePath();
        }
    }

    public ArrayList path() {
        if (!this.pathEnabled) {
            throw new UnsupportedOperationException("To use path(), you must call enablePath() before iteration begins.");
        }
        ArrayList pathElements = getPathToHere();
        int size = pathElements.size();
        if (size == 0 || pathElements.get(size - 1) != getCurrentEnd()) {
            pathElements.add(getCurrentEnd());
        }
        return pathElements;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public E next() {
        this.currentEnd = null;
        if (this.available) {
            this.available = false;
            this.currentEnd = this.nextEnd;
            return this.nextEnd;
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

    protected abstract E processNextStart() throws NoSuchElementException;

    protected ArrayList getPathToHere() {
        if (this.starts instanceof Path) {
            Path path = (Path)this.starts;
            return path.path();
        } else {
            return new ArrayList();
        }
    }

    protected E getCurrentEnd() {
        return this.currentEnd;
    }
}

