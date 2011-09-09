package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.Pair;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ReducePipe<S, T> extends AbstractPipe<S, S> implements SideEffectPipe<S, T> {

    private final PipeFunction<Pair<S, T>, T> reduceFunction;
    private T total;

    public ReducePipe(final T first, final PipeFunction<Pair<S, T>, T> reduceFunction) {
        this.total = first;
        this.reduceFunction = reduceFunction;
    }

    public S processNextStart() {
        final S s = this.starts.next();
        this.total = this.reduceFunction.compute(new Pair<S, T>(s, this.total));
        return s;
    }

    public T getSideEffect() {
        return this.total;
    }
}
