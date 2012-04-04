package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.iterators.SingleExpandableIterator;

import java.util.Arrays;
import java.util.List;

/**
 * FutureFilterPipe will allow an object to pass through it if the object has an output from the pipe provided in the constructor of the FutureFilterPipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FutureFilterPipe<S> extends AbstractMetaPipe<S, S> implements FilterPipe<S>, MetaPipe {

    private final Pipe<S, ?> pipe;
    private final SingleExpandableIterator<S> expando = new SingleExpandableIterator<S>();

    public FutureFilterPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
        this.pipe.setStarts(this.expando);

    }

    public S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            this.pipe.reset();
            this.expando.add(s);
            if (this.pipe.hasNext()) {
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
