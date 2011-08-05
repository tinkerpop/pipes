package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.SingleIterator;

import java.util.Arrays;
import java.util.List;

/**
 * FutureFilterPipe will allow an object to pass through it if the object has an output from the pipe provided in the constructor of the FutureFilterPipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FutureFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S>, MetaPipe {

    private final Pipe<S, ?> pipe;
    private final boolean doFuture;

    public FutureFilterPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
        this.doFuture = true;
    }

    public FutureFilterPipe(final Pipe<S, ?> pipe, final boolean doFuture) {
        this.pipe = pipe;
        this.doFuture = doFuture;
    }

    public S processNextStart() {
        while (true) {
            S s = this.starts.next();
            pipe.reset();
            pipe.setStarts(new SingleIterator<S>(s));
            if (pipe.hasNext()) {
                if (this.doFuture) {
                    return s;
                }
            } else if (!this.doFuture) {
                return s;
            }
        }
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipe);
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(this.pipe);
    }

}
