package com.tinkerpop.pipes.util;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class HasNextPipe<S> extends AbstractPipe<S, Boolean> {

    private final Pipe<S, ?> pipe;

    public HasNextPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
    }

    public Boolean processNextStart() {
        final S s = this.starts.next();
        this.pipe.setStarts(new SingleIterator<S>(s));
        if (this.pipe.hasNext()) {
            this.pipe.next();
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
