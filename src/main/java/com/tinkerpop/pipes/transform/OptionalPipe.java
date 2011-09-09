package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.SingleIterator;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * OptionalPipe will compute the incoming object within the internal pipe.
 * It is similar to BackFilterPipe, except that no filtering occurs.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OptionalPipe<S> extends AbstractPipe<S, S> implements MetaPipe {

    private final Pipe<S, ?> pipe;

    public OptionalPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
    }

    public S processNextStart() {
        final S s = this.starts.next();
        this.pipe.setStarts(new SingleIterator<S>(s));

        try {
            while (true) {
                pipe.next();
            }
        } catch (final NoSuchElementException e) {
        }
        return s;
    }


    public String toString() {
        return PipeHelper.makePipeString(this, this.pipe);
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(this.pipe);
    }

}