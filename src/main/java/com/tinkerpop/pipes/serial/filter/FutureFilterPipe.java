package com.tinkerpop.pipes.serial.filter;

import com.tinkerpop.pipes.serial.AbstractPipe;
import com.tinkerpop.pipes.serial.Pipe;
import com.tinkerpop.pipes.serial.SingleIterator;

import java.util.NoSuchElementException;

/**
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
                this.pipe.clear();
                return s;

            }
        }
        throw new NoSuchElementException();
    }
}
