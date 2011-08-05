package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.SingleIterator;

import java.util.Arrays;
import java.util.List;

/**
 * The OrFilterPipe takes a collection of pipes that emit boolean objects. Each pipe in the collection is fed the same incoming S object.
 * If one of the internal pipes emits true, then the OrFilterPipe emits the S object. If not, then the incoming object is not emitted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S>, MetaPipe {

    private final List<Pipe<S, Boolean>> pipes;


    public OrFilterPipe(final Pipe<S, Boolean>... pipes) {
        this.pipes = Arrays.asList(pipes);
    }

    public OrFilterPipe(final List<Pipe<S, Boolean>> pipes) {
        this.pipes = pipes;
    }

    public S processNextStart() {
        while (true) {
            S s = this.starts.next();
            for (Pipe<S, Boolean> pipe : this.pipes) {
                pipe.setStarts(new SingleIterator<S>(s));
                if (pipe.next()) {
                    return s;
                }
            }
        }
    }

    public List<Pipe> getPipes() {
        return (List) this.pipes;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipes);
    }
}
