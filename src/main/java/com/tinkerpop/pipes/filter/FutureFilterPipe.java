package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;

import java.util.NoSuchElementException;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FutureFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final Pipe<S, ?> pipe;

    public FutureFilterPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
    }

    public S processNextStart() {
        while (true) {
            S s = this.starts.next();
            pipe.setStarts(new SingleIterator<S>(s));
            if (pipe.hasNext()) {
                return s;
            }
        }
    }
}
