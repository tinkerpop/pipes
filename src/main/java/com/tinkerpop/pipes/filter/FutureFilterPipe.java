package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;

import java.util.NoSuchElementException;

/**
 * The FutureFilterPipe will determine whether to emit its incoming object if and only if the provided pipe emits an object.
 * In other words, the provided pipe must have a next() object for each incoming object to the FutureFilterPipe.
 * If it does, then the FutureFilterPipe emits the object it receives. If not, then it does not.
 * This pipe is useful for searching a data flow path for a value -- hence the name, "future filter."
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FutureFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final Pipe<S, ?> pipe;

    public FutureFilterPipe(Pipe<S, ?> pipe) {
        this.pipe = pipe;
    }

    protected S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            this.pipe.setStarts(new SingleIterator<S>(s));
            if (this.pipe.hasNext()) {
                this.pipe.next();
                return s;

            }
        }
        throw new NoSuchElementException();
    }
}
