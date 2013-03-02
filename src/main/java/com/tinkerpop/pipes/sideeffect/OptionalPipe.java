package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.AbstractMetaPipe;
import com.tinkerpop.pipes.util.MetaPipe;
import com.tinkerpop.pipes.util.PipeHelper;
import com.tinkerpop.pipes.util.iterators.SingleExpandableIterator;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * OptionalPipe will compute the incoming object within the internal pipe.
 * It is similar to BackFilterPipe, except that no filtering occurs.
 * Moreover, it is a SideEffectPipe in that it is only useful for its side-effect (internal pipe) behavior.
 * However, getSideEffect() simply returns null as there is no internal data structure.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OptionalPipe<S> extends AbstractMetaPipe<S, S> implements SideEffectPipe.LazySideEffectPipe<S, Object>, MetaPipe {

    private final Pipe<S, ?> pipe;
    private final SingleExpandableIterator<S> expando = new SingleExpandableIterator<S>();

    public OptionalPipe(final Pipe<S, ?> pipe) {
        this.pipe = pipe;
        this.pipe.setStarts(this.expando);
    }

    public S processNextStart() {
        final S s = this.starts.next();
        this.expando.add(s);
        try {
            while (true) {
                this.pipe.next();
            }
        } catch (final NoSuchElementException e) {
        }
        return s;
    }

    /**
     * The side effect is the behavior of the internal pipe which is not a gettable data structure.
     *
     * @return will return null
     */
    public Object getSideEffect() {
        return null;
    }

    public String toString() {
        return PipeHelper.makePipeString(this, this.pipe);
    }

    public List<Pipe> getPipes() {
        return (List) Arrays.asList(this.pipe);
    }

}