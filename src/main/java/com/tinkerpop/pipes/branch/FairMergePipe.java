package com.tinkerpop.pipes.branch;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.MetaPipe;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * FairMergePipe will, in a round robin fashion, emit the the objects of its internal pipes.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FairMergePipe<E> extends AbstractPipe<E, E> implements MetaPipe {

    private final List<Pipe> pipes;
    int current = 0;
    final int total;

    public FairMergePipe(final List<Pipe> pipes) {
        this.pipes = pipes;
        this.total = pipes.size();
    }

    public E processNextStart() {
        int counter = 0;
        while (true) {
            counter++;
            final Pipe currentPipe = this.pipes.get(this.current);
            if (currentPipe.hasNext()) {
                final E e = (E) currentPipe.next();
                this.current = (this.current + 1) % this.total;
                return e;
            } else if (counter == this.total) {
                throw new NoSuchElementException();
            } else {
                this.current = (this.current + 1) % this.total;
            }
        }
    }

    public void reset() {
        for (final Pipe pipe : this.pipes) {
            pipe.reset();
        }
        super.reset();
    }


    public List<Pipe> getPipes() {
        return this.pipes;
    }

    public String toString() {
        return super.toString() + "[" + this.pipes + "]";
    }


}
