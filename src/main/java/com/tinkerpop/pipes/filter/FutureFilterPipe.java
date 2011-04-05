package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;

/**
 * FutureFilterPipe will allow an object to pass through it if the object has an output from the pipe provided in the constructor of the FutureFilterPipe.
 *
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
                pipe.reset();
                return s;
            }
        }
    }

    public String toString() {
        return super.toString() + "<" + this.pipe + ">";
    }

}
