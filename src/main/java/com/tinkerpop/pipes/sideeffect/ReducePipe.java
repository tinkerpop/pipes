package com.tinkerpop.pipes.sideeffect;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.PipeFunction;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class ReducePipe<S> extends AbstractPipe<S, S> implements SideEffectPipe<S, S> {

    private final PipeFunction<Tuple<S>, S> reduceFunction;
    private S total;

    public ReducePipe(final S first, final PipeFunction<Tuple<S>, S> reduceFunction) {
        this.total = first;
        this.reduceFunction = reduceFunction;
    }

    public S processNextStart() {
        final S s = this.starts.next();
        this.total = this.reduceFunction.compute(new Tuple<S>(this.total, s));
        return s;
    }

    public S getSideEffect() {
        return this.total;
    }

    public class Tuple<A> {
        public final A a;
        public final A b;

        public Tuple(final A a, final A b) {
            this.a = a;
            this.b = b;
        }
    }
}
