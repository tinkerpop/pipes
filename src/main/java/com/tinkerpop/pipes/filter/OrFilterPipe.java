package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.SingleIterator;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The OrFilterPipe takes a collection of FilterPipes. Each FilterPipe is fed the same incoming object.
 * If one of the FilterPipes emit the object, then the OrFilterPipe emits the object. If not, then the incoming object is not emitted.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OrFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final List<Pipe<S, Boolean>> pipes;


    public OrFilterPipe(final Pipe<S, Boolean>... pipes) {
        this.pipes = Arrays.asList(pipes);
    }

    public OrFilterPipe(final List<Pipe<S, Boolean>> pipes) {
        this.pipes = pipes;
    }

    public S processNextStart() {
        while (this.starts.hasNext()) {
            S s = this.starts.next();
            for (Pipe<S, Boolean> pipe : this.pipes) {
                pipe.setStarts(new SingleIterator<S>(s));
                if (pipe.next()) {
                    return s;
                }
            }
        }
        throw new NoSuchElementException();
    }

}
