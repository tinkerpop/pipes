package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.iterators.SingleExpandableIterator;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * BackFilterPipe will fully process the object through its internal pipe.
 * If the internal pipe yields results, then the original object is emitted from the BackFilterPipe.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BackFilterPipe<S> extends AbstractMetaPipe<S, S> implements FilterPipe<S>, MetaPipe {

    private final Pipe<S, ?> pipe;
    private final SingleExpandableIterator<S> expando = new SingleExpandableIterator<S>();

    public BackFilterPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
        this.pipe.setStarts(this.expando);
    }

    public S processNextStart() {
        while (true) {
            final S s = this.starts.next();
            this.expando.add(s);
            if (this.pipe.hasNext()) {
                try {
                    while (true) {
                        this.pipe.next();
                    }
                } catch (final NoSuchElementException e) {
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