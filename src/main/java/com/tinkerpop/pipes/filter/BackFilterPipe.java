package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.SingleIterator;

import java.util.Arrays;
import java.util.List;

/**
 * BackFilterPipe will fully process the object through its internal pipe.
 * If the internal pipe yields results, then the original object is emitted from the BackFilterPipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BackFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S>, MetaPipe {

    private final Pipe<S, ?> pipe;

    public BackFilterPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
    }

    public S processNextStart() {
        while (true) {
            S s = this.starts.next();
            this.pipe.setStarts(new SingleIterator<S>(s));
            if (pipe.hasNext()) {
                while (pipe.hasNext()) {
                    pipe.next();
                }
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