package com.tinkerpop.pipes.transform;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.iterators.SingleExpandableIterator;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @author Darrick Wiebe (http://ofallpossibleworlds.wordpress.com)
 */
public class HasNextPipe<S> extends AbstractMetaPipe<S, Boolean> implements MetaPipe, TransformPipe<S, Boolean> {

    private Pipe<S, ?> pipe;
    private final SingleExpandableIterator<S> expando = new SingleExpandableIterator<S>();

    /**
     * Initialize this pipe to apply its conditions to the given pipe.
     * Each object in the source pipe is fed into the given pipe individually.
     *
     * @param pipe The pipe to determine if objects exist after input processed
     */
    public HasNextPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
        this.pipe.setStarts(this.expando);
    }

    public Boolean processNextStart() {
        final S s = this.starts.next();
        this.pipe.reset();
        this.expando.add(s);
        return this.pipe.hasNext();

    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(this.pipe);
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipe);
    }
}
